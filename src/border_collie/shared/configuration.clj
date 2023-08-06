(ns border-collie.shared.configuration
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def home-directory (io/file (System/getProperty "user.home")))
(def config-file (io/file home-directory ".border-collie/config.edn"))

(def base-config {:services-path ""})

(defn expand-home-directory
  [path]
  (if (string/starts-with? path "~/")
    (string/replace-first path #"^~" (.getAbsolutePath home-directory))
    path))

(defn write-to-file
  [configuration]
  (io/make-parents config-file)
  (spit config-file (pr-str configuration))
  configuration)

(defn exists?
  []
  (.exists config-file))

(defn load-from-file
  []
  (if (exists?)
    (read-string (slurp config-file))
    (write-to-file base-config)))

(defn init
  []
  (-> (load-from-file)
      (update :services-path expand-home-directory)))
