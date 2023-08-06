(ns border-collie.service.service-task
  (:require [clojure.core.async :as async]
            [clojure.java.io :as io]
            [border-collie.shared.process :as process]
            )
  (:import (java.io File InputStream)
           (java.util Scanner)))

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
        process (process/start {:err :stdout
                                :dir (.getAbsolutePath ^File (:file service))}
                               "rake"
                               "app:start")
        log (atom [])]

    (async/go
      (let [scanner (Scanner. ^InputStream (:out process))]
        (while (.hasNextLine scanner)
          (async/>! channel (.nextLine scanner)))))

    (async/go-loop []
      (when-some [message (async/<! channel)]
        (println message)
        (swap! log conj message)
        (recur)))

    {:process process
     :channel channel
     :*log log}))

