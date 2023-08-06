(ns border-collie.app.core
  (:require [border-collie.design-system.core :as ds]
            [border-collie.home.screen :as home.screen]
            [border-collie.router :as router]
            [border-collie.settings.screen :as settings.screen]
            [clojure.java.io :as io]
            [border-collie.app.state :as state]
            [clojure.string :as string]))

(def home-directory (io/file (System/getProperty "user.home")))
(def config-file (io/file home-directory ".border-collie/config.edn"))

(def base-config {:services-path ""})

(defn expand-home-directory
  [path]
  (if (string/starts-with? path "~/")
    (string/replace-first path #"^~" (.getAbsolutePath home-directory))
    path))

(defn write-config-to-file
  [configuration]
  (io/make-parents config-file)
  (spit config-file (pr-str configuration))
  configuration)

(defn load-config-from-file
  []
  (if (.exists config-file)
    (read-string (slurp config-file))
    (write-config-to-file base-config)))

(defn initialise-config
  []
  (-> (load-config-from-file)
      (update :services-path expand-home-directory)))

(defn init
  []
  (let [config (initialise-config)]
    (state/init config)
    (ds/with-theme
      (router/render {:*router (router/create
                                 {:home     home.screen/render
                                  :settings settings.screen/render}
                                 {:route :home})
                      :*state  state/*state}))))