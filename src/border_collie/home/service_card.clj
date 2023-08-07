(ns border-collie.home.service-card
  (:require [border-collie.app.state :as app-state]
            [border-collie.design-system.core :as ds]
            [border-collie.home.commands :as commands]
            [io.github.humbleui.ui :as ui]))

(defn service-task-status->text
  [service-task]
  (case (:status service-task)
    :stopping "stopping"
    :stopped "stopped"
    :died "died"
    :running "running"
    ""))

(defn create-on-run
  [*state service]
  (fn []
    (commands/start-service *state service)))

(defn create-on-stop
  [*state service-task]
  (fn []
    (commands/stop-service *state service-task)))

(defn create-component
  [{:keys [*state]} service]
  (ui/dynamic
    _
    [service-task (app-state/get-service-task *state service)]
    (ui/padding
      8 8
      (ds/card
        (ui/column
          (ui/halign
            0.5
            (ds/label {:role :body-medium} (:name service)))
          (ui/gap 0 8)
          (ui/halign
            0.5
            (ds/label {:role :body-small} (service-task-status->text service-task)))
          (ui/gap 0 8)
          (ui/halign
            1
            (ui/row
              (if (= (:status service-task) :running)
                (ds/button (create-on-stop *state service-task) "Stop")
                (ds/button (create-on-run *state service) "Run")))))))))
