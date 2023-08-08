(ns border-collie.shared.command
  (:require [clojure.java.process :as process]
            [border-collie.shared.process-handle :as process-handle]))

(defn alive?
  [command]
  (process-handle/alive? (process-handle/process->handle (:process command))))

(defn stop
  [command]
  (process-handle/stop-handle-and-children
    (process-handle/process->handle (:process command))))

(defn start
  [& opts+args]
  (apply process/start opts+args))

(defn to-file
  [f & opts]
  (process/to-file f opts))