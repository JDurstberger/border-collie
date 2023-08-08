(ns border-collie.logs
  (:require [border-collie.shared.command :as command])
  (:import (java.io File InputStream)
           (java.util Scanner)))

(defn create-file-reader
  [^File file handler-fn]
  (let [file-path (.getAbsolutePath file)
        command (command/start "tail" "-F" file-path)
        scanner (Scanner. ^InputStream (:out command))]

    (future
      (while (.hasNextLine scanner)
        (handler-fn (.nextLine scanner)))
      (println "Scanner for" file-path "closed"))

    {:command   command
     :file-path file-path}))

(defn shutdown
  [file-reader]
  (println "Shutting down file reader for " (:file-path file-reader))
  (command/stop (:command file-reader)))