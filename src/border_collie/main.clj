(ns border-collie.main
  (:require [border-collie.design-system.core :as ds]
            [io.github.humbleui.ui :as ui]
            [border-collie.router :as router]
            [border-collie.home.screen :as home.screen]
            [border-collie.home.commands :as home.commands]
            [border-collie.settings.screen :as settings.screen]
            [border-collie.state :as state]))

(defonce *router (router/create {:home     home.screen/render
                                 :settings settings.screen/render}
                                {:route :home}))

(def app
  (ds/with-theme
    (router/render {:*router *router
                    :*state  state/*state})))

;; reset current app state on eval of this ns
(reset! state/*app app)

(defn -main
  "Run once on app start, starting the humble app."
  [& args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. ^Runnable (fn []
                                         (home.commands/stop-all-services state/*state))))
  (ui/start-app!
    (reset! state/*window
            (ui/window
              {:title    "Border Collie"
               :bg-color 0xFFFFFFFF}
              state/*app)))
  (state/redraw!))