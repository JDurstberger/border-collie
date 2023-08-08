(ns border-collie.home.service-task
  (:require [border-collie.logs :as logs]
            [border-collie.shared.command :as command]
            [clojure.java.io :as io]))

(defn clean-up
  [service-task]
  (logs/shutdown (:log-reader service-task)))

(defn stop-service
  [service-task]
  (when-let [command (:command service-task)]
    (if (command/alive? command)
      (do
        (command/stop command)
        (clean-up service-task))
      (println "Service has already exited"))))

(defn create
  [service]
  {:service-id (:id service)
   :status     :idle})

(defn run-service
  [service]
  (let [log-file (io/file (str "logs/" (:name service)))
        _ (io/make-parents log-file)
        command (command/start {:out (command/to-file log-file)
                                :err :stdout
                                :dir (.getAbsolutePath (:file service))}
                               "rake"
                               "app:start")
        log-reader (logs/create-file-reader log-file (fn [_]))]
    {:service-id (:id service)
     :status     :running
     :command    command
     :log-reader log-reader}))
