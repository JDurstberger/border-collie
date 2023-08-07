(ns border-collie.home.commands
  (:require [border-collie.app.state :as app-state]
            [border-collie.home.service-task :as service-task]
            [clojure.core.async :as async]
            [clojure.java.io :as io]))

(defn find-all-service-directories
  [path]
  (println "Loading services from " path)
  (->> (io/file path)
       (.listFiles)
       (filter #(re-matches #".*-(service|backend|gateway)$" (.getName %)))))

(defn service-file->service
  [service-file]
  {:file service-file
   :id   (keyword (.getName service-file))
   :name (.getName service-file)})

(defn load-services
  [*state]
  (when (empty? (:services @*state))
    (let [configuration (:configuration @*state)]
      (let [service-files (find-all-service-directories (:services-path configuration))
            services (->> service-files
                          (map service-file->service)
                          (map (fn [service] [(:id service) service]))
                          (into (sorted-map)))]
        (app-state/upsert-services *state services)
        (app-state/upsert-service-tasks *state (into {} (map service-task/create services)))))))

(defn start-service
  [*state service]
  (let [service-task (service-task/run-service service)]

    (async/go
      (println (:name service) " started")

      @(:process service-task)

      (let [service-task (app-state/get-service-task *state service)
            new-status (if (= (:status service-task) :stopping) :stopped :died)]
        (println (:name service) (name new-status))
        (app-state/upsert-service-task *state (assoc service-task :status new-status))))

    (app-state/upsert-service-task *state service-task)))

(defn stop-service
  [*state service-task]
  (app-state/upsert-service-task *state (assoc service-task :status :stopping))
  (service-task/stop-service service-task))

(defn stop-all-services
  [*state]
  (doseq [service-task (-> @*state :service-tasks)]
    (service-task/stop-service service-task))
  (swap! *state assoc :service-tasks {}))