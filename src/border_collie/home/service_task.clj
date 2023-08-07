(ns border-collie.home.service-task
  (:require [border-collie.shared.process :as process]
            [clojure.java.io :as io]))

(defn stop-service
  [service-task]
  (when-let [process (-> service-task :process :process)]
    (if (process/alive? process)
      (process/stop-process-and-children process)
      (println "Service has already exited"))))

(defn create
  [service]
  {:service-id (:id service)
   :status     :idle})

(defn run-service
  [service]
  (let [log-file (io/file (str "logs/" (:name service)))
        _ (io/make-parents log-file)
        process (process/start {:out (process/to-file log-file)
                                :err :stdout
                                :dir (.getAbsolutePath (:file service))}
                               "rake"
                               "app:start")]
    {:service-id (:id service)
     :status     :running
     :process    process}))
