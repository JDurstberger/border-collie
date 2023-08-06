(ns border-collie.shared.stream
  (:import (java.util.stream Stream)))

(defn ->seq
  [^Stream stream]
  (-> stream
      .iterator
      iterator-seq))
