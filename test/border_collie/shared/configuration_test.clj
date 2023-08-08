(ns border-collie.shared.configuration-test
  (:require [border-collie.shared.configuration :as configuration]
            [clojure.test :refer :all]))

(def current-users-home-directory (System/getProperty "user.home"))

(deftest configuration-update-only-modifies-updated-entries
  (let [configuration {:some-config-value  123
                       :other-config-value "abc"}
        new-configuration (configuration/update-configuration configuration {:some-config-value 890})]

    (is (= {:some-config-value  890
            :other-config-value "abc"}
           new-configuration))))

(deftest configuration-update-expands-paths
  (let [configuration {}
        new-configuration (configuration/update-configuration configuration {:services-path "~/some/sub/dir"})]

    (is (= {:services-path (str current-users-home-directory "/some/sub/dir")}
           new-configuration))))