(ns border-collie.home.screen
  (:require
    [border-collie.design-system.core :as ds]
    [border-collie.home.commands :as commands]
    [border-collie.router :as router]
    [io.github.humbleui.ui :as ui]))

(defn on-render
  [*state]
  (commands/load-services *state))

(defn create-service-card
  [{:keys [*router]} [service-key service]]
  (ui/padding
    8 8
    (ds/card
      (ui/column
        (ui/halign
          0.5
          (ds/label {:role :body-medium} (:name service)))
        (ui/gap 0 8)
        (ui/row
          [:stretch 1 (ds/button #(router/navigate-to *router {:route :service
                                                               :extras {:service-key service-key}}) "Details")]
          (ui/gap 8 0)
          [:stretch 1 (ds/button nil "Run")])))))


(defn render
  [{:keys [*state] :as dependencies}]
  (on-render *state)
  (ds/screen
    (ui/dynamic
      _
      [services (-> @*state :services)]
      (ui/vscroll
        (ui/column
          (map (partial create-service-card dependencies) services))))))





