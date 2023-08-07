(ns border-collie.app.state)

(defonce *state (atom {}))

(defn upsert-services
  [*state services]
  (swap! *state assoc :services services))

(defn upsert-service-task
  [*state service-task]
  (swap! *state
         assoc-in [:service-tasks (:service-id service-task)] service-task))

(defn remove-service-task
  [*state service-task]
  (swap! *state update :service-tasks dissoc (:service-id service-task)) )

(defn get-service-task
  [*state service]
  (get-in @*state [:service-tasks (:id service)]))

(defn init
  [configuration]
  (reset! *state {:configuration configuration
                  :services      {}
                  :service-tasks {}}))