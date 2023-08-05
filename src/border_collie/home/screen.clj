(ns border-collie.home.screen
  (:require [border-collie.router :as router]
            [border-collie.design-system.core :as ds]
            [io.github.humbleui.ui :as ui]))

(defn render
  [{:keys [router]}]
  (ds/screen
    (ui/column

      (ds/card (ui/label "A A A "))
      (ds/card (ui/label "A A A "))
      (ds/card (ui/label "A A A "))
      (ds/card (ui/label "A A A "))

      (ds/button (fn [] (router/navigate-to router :service)) "GO TO SERVICE"))))
