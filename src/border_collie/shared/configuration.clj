(ns border-collie.shared.configuration
  (:require
    [border-collie.shared.environment :as environment]
    [me.raynes.fs :as fs]))

(defn get-home-directory
  []
  (or (fs/file (environment/get-variable "BORDER_COLLIE_HOME"))
      (fs/file (fs/home) ".border-collie")))

(defn get-configuration-file
  []
  (fs/file (get-home-directory) "configuration.edn"))

(def paths [:services-path])

(def base-config {:services-path   ""
                  :ignore-services ""})

(defn write-to-file
  [configuration]
  (let [configuration-file (get-configuration-file)]
    (fs/mkdirs (fs/parent configuration-file))
    (spit configuration-file (pr-str configuration))
    configuration))

(defn exists?
  []
  (.exists (get-configuration-file)))

(defn load-from-file
  []
  (if (exists?)
    (read-string (slurp (get-configuration-file)))
    (write-to-file base-config)))

(defn expand-paths
  [configuration]
  (reduce (fn [configuration path-key]
            (if (path-key configuration)
              (update configuration path-key (fn [path] (.getAbsolutePath (fs/expand-home path))))
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
