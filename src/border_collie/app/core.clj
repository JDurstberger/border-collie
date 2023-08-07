(ns border-collie.app.core
  (:require [border-collie.design-system.core :as ds]
            [border-collie.home.screen :as home.screen]
            [border-collie.router :as router]
            [border-collie.settings.screen :as settings.screen]
            [border-collie.shared.configuration :as configuration]
            [border-collie.app.state :as state]))

(defn init
  []
  (let [initial-screen (if (configuration/exists?) :home :settings)
        config (configuration/init)]
    (state/init config)
    (ds/with-theme
      (router/render {:*router (router/create
                                 {:home     home.screen/render
                                  :settings settings.screen/render}
                                 {:route initial-screen})
                      :*state  state/*state}))))