(ns border-collie.service.screen
  (:require [border-collie.design-system.core :as ds]
            [border-collie.router :as router]
            [border-collie.service.service-task :as service-task]
            [io.github.humbleui.ui :as ui]))

(defn create-on-back
  [*router]
  (fn []
    (router/navigate-to *router :home)))

(defn create-on-run
  [*state service]
  (fn []
    (swap! *state assoc-in [:service-tasks (:id service)] (service-task/run-service service))))

(defn create-on-stop
  [*state service service-task]
  (fn []
    (service-task/stop-service service-task)
    (swap! *state update :service-tasks dissoc (:id service))))

(defn create-log-component
  [service-task]
  (ui/height
    200
    (ui/vscroll
      (ui/column
        (for [log-entry @(or (:*log service-task) (atom []))]
          (ds/label {:role :body-medium} log-entry))))))

(defn render
  [{:keys [*router *state]}]
  (let [selected-service-key (-> @*router :active-route :extras :service-key)]
    (ds/screen
      (ui/dynamic
        _
        [service (get-in @*state [:services selected-service-key])
         service-task (get-in @*state [:service-tasks selected-service-key])]
        (do
          (ui/column
            (ui/halign
              0.5
              (ui/padding
                8 8 8 16
                (ds/label {:role :headline-medium} (:name service))))
            (if service-task
              (ds/button (create-on-stop *state service service-task) "Stop")
              (ds/button (create-on-run *state service) "Run"))
            (create-log-component service-task)
            (ds/button (create-on-back *router) "Back")))))))
