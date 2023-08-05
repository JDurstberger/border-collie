(ns border-collie.service.screen
  (:require [border-collie.router :as router]
            [io.github.humbleui.ui :as ui]))

(defn render
  [{:keys [router]}]
  (ui/column
    (ui/label "Service")
    (ui/button (fn [] (router/navigate-to router :home))
               (ui/label "back"))))
