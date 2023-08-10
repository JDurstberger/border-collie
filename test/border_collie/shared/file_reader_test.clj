(ns border-collie.shared.file-reader-test
  (:require [me.raynes.fs :as fs]
            [border-collie.shared.file-reader :as file-reader]
            [awaitility-clj.core :as awaitility]
            [clojure.test :refer :all]))

(def temp-dir (fs/temp-dir "file-reader-tests"))

(def *file-reader (atom nil))
(defn create-file-reader
  [file f]
  (reset! *file-reader (file-reader/create file f)))

(defn with-file-reader
  [*file-reader]
  (fn [f]
    (try (f)
         (catch Exception _
           (when-let [file-reader @*file-reader]
             (file-reader/shutdown file-reader))))))

(use-fixtures :each
              (with-file-reader *file-reader))

(defn write-line
  [file line]
  (spit file (str line "\n")))

(deftest something
  (let [file (fs/file temp-dir "a-file.txt")
        *lines (atom [])
        line "a line"
        _ (fs/create file)
        _ (create-file-reader file (fn [line]
                                     (swap! *lines conj line)))]

    (write-line file line)

    (awaitility/wait-for {:at-most [500 :milliseconds]}
                         #(not (empty? @*lines)))
    (is (= line (first @*lines)))))