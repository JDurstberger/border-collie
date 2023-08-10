(ns border-collie.shared.configuration-test
  (:require [border-collie.shared.configuration :as configuration]
            [clojure.test :refer :all]
            [me.raynes.fs :as fs]))

(def temp-home (fs/temp-dir "configuration-test"))
(def current-users-home-directory (System/getProperty "user.home"))

(deftest home-is-in-users-home-by-default
  (= (str (fs/home) ".border-collie") (configuration/get-home-directory)))


(deftest home-can-be-changed-with-environment-variable
  (with-redefs [border-collie.shared.environment/get-variable (fn [name]
                                                                (when (= name "BORDER_COLLIE_HOME")
                                                                  "/some-folder"))]
    (= (str "/some-folder/.border-collie") (configuration/get-home-directory))))

(deftest configuration-update-only-modifies-updated-entries
  (with-redefs [border-collie.shared.environment/get-variable (constantly temp-home)]
    (let [configuration {:some-config-value  123
                         :other-config-value "abc"}
          new-configuration (configuration/update-configuration configuration {:some-config-value 890})]

      (is (= {:some-config-value  890
              :other-config-value "abc"}
             new-configuration)))))

(deftest configuration-update-expands-paths
  (with-redefs [border-collie.shared.environment/get-variable (constantly temp-home)]
    (let [configuration {}
          new-configuration (configuration/update-configuration configuration {:services-path "~/some/sub/dir"})]

      (is (= {:services-path (str current-users-home-directory "/some/sub/dir")}
             new-configuration)))))