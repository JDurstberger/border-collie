(ns border-collie.shared.process
  (:require [border-collie.shared.stream :as stream]
            [clojure.java.process :as process])
  (:import (java.lang ProcessHandle)))

(defn children
  [^ProcessHandle process]
  (stream/->seq (.children process)))

(defn do-to-children
  [^ProcessHandle handle f]
  (doseq [child (children handle)]
    (do
      (do-to-children child f)
      (f child))))

(defn do-to-process-and-children
  [^ProcessHandle handle f]
  (do-to-children handle f)
  (f handle))

(defn alive?
  [process-or-handle]
  (.isAlive process-or-handle))

(defn stop
  [^ProcessHandle handle]
  (when (alive? handle)
    (.destroy handle)))

(defn process->handle
  [process-or-handle]
  (if-not (= (type process-or-handle) ProcessHandle)
    (.toHandle process-or-handle)
    process-or-handle))

(defn stop-process-and-children
  [process-or-handle]
  (do-to-process-and-children
    (process->handle process-or-handle)
    stop))

(defn start
  [& opts+args]
  (apply process/start opts+args))

(defn to-file
  [f & opts]
  (process/to-file f opts))