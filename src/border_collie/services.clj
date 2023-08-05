(ns border-collie.services
  (:require [clojure.java.io :as io]))

(defn load-services
  []
  (doseq [file (.listFiles (io/file "."))]
    (println file)))

(load-services)