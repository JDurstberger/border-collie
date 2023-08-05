(ns border-collie.home.screen
  (:require [border-collie.router :as router]
            [border-collie.design-system.core :as ds]
            [border-collie.home.commands :as commands]
            [io.github.humbleui.ui :as ui]
            [io.github.humbleui.ui.dynamic :as dynamic]))

(defn on-render
  [*state]
  (commands/load-services *state))

(defn create-service-card
  [service]
  (ds/card
    (ui/halign
      0.5
      (ds/label {:role :body-medium} (:name service)))))

(defn render
  [{:keys [*state]}]
  (on-render *state)
  (ds/screen
    (ui/dynamic
      _
      [services (:services @*state)]
      (do
        (ui/column
          (map create-service-card services))))))





