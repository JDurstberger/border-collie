(ns border-collie.home.commands
  (:require [clojure.java.io :as io]))

(defn find-all-service-directories
  [path]
  (->> (io/file path)
       (.listFiles)
       (filter #(re-matches #".*-(service|backend|gateway)$" (.getName %)))))

(defn service-file->service
  [service-file]
  {:file service-file
   :name (.getName service-file)})

(defn load-services
  [*state]
  (when (empty? (:services @*state))
    (let [configuration (:configuration @*state)]
      (let [service-files (find-all-service-directories (:services-path configuration))
            services (->> service-files
                          (map service-file->service)
                          (map (fn [service] [(:name service) service]))
                          (into (sorted-map)))]
        (swap! *state assoc :services services)))))