(ns border-collie.home.commands
  (:require [border-collie.home.service-task :as service-task]
            [clojure.java.io :as io]))

(defn find-all-service-directories
  [path]
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
        (swap! *state assoc :services services)))))

(defn start-service
  [*state service]
  (swap! *state assoc-in [:service-tasks (:id service)] (service-task/run-service service)) )

(defn stop-service
  [*state service service-task]
  (service-task/stop-service service-task)
  (swap! *state update :service-tasks dissoc (:id service)))

(defn stop-all-services
  [*state]
  (doseq [service-task (-> @*state :service-tasks)]
    (service-task/stop-service service-task))
  (swap! *state assoc :service-tasks {}))