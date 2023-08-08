(ns border-collie.shared.configuration
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def home-directory (io/file (System/getProperty "user.home")))
(def config-file (io/file home-directory ".border-collie/config.edn"))
(def paths [:services-path])

(def base-config {:services-path   ""
                  :ignore-services ""})

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

(defn expand-paths
  [configuration]
  (reduce (fn [configuration path-key]
            (if (path-key configuration)
              (update configuration path-key expand-home-directory)
              configuration))
          configuration
          paths))

(defn update-configuration
  [current-configuration updates]
  (let [new-configuration (merge current-configuration updates)]
    (write-to-file new-configuration)
    (expand-paths new-configuration)))

(defn init
  []
  (-> (load-from-file)
      (expand-paths)))
