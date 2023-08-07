(ns border-collie.home.service-task
  (:require [clojure.core.async :as async]
            [border-collie.shared.process :as process]
            [clojure.java.io :as io]))

(defn stop-service
  [service-task]
  (let [process (-> service-task :process :process)]
    (if (process/alive? process)
      (do
        (process/stop-process-and-children process)
        (async/close! (:channel service-task)))
      (println "Service has already exited"))))

(defn run-service
  [service]
  (let [channel (async/chan 100)
        log-file (io/file (str "logs/" (:name service)))
        _ (io/make-parents log-file)
        process (process/start {:out (process/to-file log-file)
                                :err :stdout
                                :dir (.getAbsolutePath (:file service))}
                               "rake"
                               "app:start")]
    {:service-id (:id service)
     :process    process
     :channel    channel}))
