(ns border-collie.service.screen
  (:require [border-collie.design-system.core :as ds]
            [border-collie.router :as router]
            [io.github.humbleui.ui :as ui]))

(defn render
  [{:keys [*router]}]
  (ds/screen
    (ui/column
      (ui/label "Service")
      (ds/button (fn [] (router/navigate-to *router :home))
                 "Back"))))
