(ns border-collie.app.state-test
  (:require [border-collie.app.state :as app-state]
            [clojure.test :refer :all]))

(deftest swap-configurations!-updates-config-with-value
  (let [update-fn #(assoc % :another-config-value "abc")]

    (app-state/init {:some-config-value 123})
    (app-state/swap-configuration! app-state/*state update-fn)

    (is (= {:some-config-value 123
            :another-config-value "abc"}
          (app-state/get-configuration app-state/*state)))))
