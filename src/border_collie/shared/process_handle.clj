(ns border-collie.shared.process-handle
  (:require [border-collie.shared.stream :as stream])
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

(defn do-to-handle-and-children
  [^ProcessHandle handle f]
  (do-to-children handle f)
  (f handle))

(defn alive?
  [^ProcessHandle handle]
  (.isAlive handle))

(defn stop
  [^ProcessHandle handle]
  (when (alive? handle)
    (.destroy handle)))

(defn stop-handle-and-children
  [^ProcessHandle handle]
  (do-to-handle-and-children handle stop))

(defn process->handle
  [^Process process]
  (.toHandle process))