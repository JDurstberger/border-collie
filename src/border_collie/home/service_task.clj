(ns border-collie.home.service-task
  (:require [border-collie.shared.file-reader :as file-reader]
            [border-collie.shared.command :as command]
            [me.raynes.fs :as fs]))

(defn clean-up
  [service-task]
  (file-reader/shutdown (:log-reader service-task)))

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
  (let [log-file (fs/file (str "logs/" (:name service)))
        _ (fs/mkdirs (fs/parent log-file))
        command (command/start {:out (command/to-file log-file)
                                :err :stdout
                                :dir (.getAbsolutePath (:file service))}
                               "rake"
                               "app:start")
        log-reader (file-reader/create log-file (fn [_]))]
    {:service-id (:id service)
     :status     :running
     :command    command
     :log-reader log-reader}))
