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
        (app-state/upsert-services *state services)))))

(defn start-service
  [*state service]
  (let [service-task (service-task/run-service service)]

    (async/go
      (println (:name service) " started")
      (let [exit-code @(:process service-task)]
        (println (:name service) " stopped with " exit-code)
        (app-state/remove-service-task *state service-task)))

    (app-state/upsert-service-task *state service-task)))

(defn stop-service
  [*state service-task]
  (service-task/stop-service service-task)
  (app-state/remove-service-task *state service-task))

(defn stop-all-services
  [*state]
  (doseq [service-task (-> @*state :service-tasks)]
    (service-task/stop-service service-task))
  (swap! *state assoc :service-tasks {}))