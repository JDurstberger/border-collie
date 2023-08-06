(ns border-collie.home.service-card
  (:require [border-collie.design-system.core :as ds]
            [border-collie.home.service-task :as service-task]
            [io.github.humbleui.ui :as ui]))

(defn create-on-run
  [*state service]
  (fn []
    (swap! *state assoc-in [:service-tasks (:id service)] (service-task/run-service service))))

(defn create-on-stop
  [*state service service-task]
  (fn []
    (service-task/stop-service service-task)
    (swap! *state update :service-tasks dissoc (:id service))))

(defn create-component
  [{:keys [*state]} service]
  (println service)
  (ui/dynamic
    _
    [service-task (get-in @*state [:service-tasks (:id service)])]
    (ui/padding
      8 8
      (ds/card
        (ui/column
          (ui/halign
            0.5
            (ds/label {:role :body-medium} (:name service)))
          (ui/gap 0 8)
          (ui/halign
            1
            (ui/row
              (if service-task
                (ds/button (create-on-stop *state service service-task) "Stop")
                (ds/button (create-on-run *state service) "Run")))))))))
