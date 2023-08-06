(ns border-collie.service.screen
  (:require [border-collie.design-system.core :as ds]
            [border-collie.router :as router]
            [io.github.humbleui.ui :as ui]))

(defn create-on-back
  [*router]
  (fn []
    (router/navigate-to *router :home)))

(defn render
  [{:keys [*router *state]}]
  (let [selected-service-key (-> @*router :active-route :extras :service-key)]
    (ds/screen
      (ui/dynamic
        _
        [service (get-in @*state [:services selected-service-key])]
        (ui/column
          (ui/halign
            0.5
            (ui/padding
              8 8 8 16
              (ds/label {:role :headline-medium} (:name service))))
          (ds/button (create-on-back *router) "Back"))))))
