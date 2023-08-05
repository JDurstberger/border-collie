(ns border-collie.main
  (:require [border-collie.design-system.core :as ds]
            [io.github.humbleui.ui :as ui]
            [io.github.humbleui.paint :as paint]
            [border-collie.router :as router]
            [border-collie.home.screen :as home.screen]
            [border-collie.service.screen :as service.screen]
            [border-collie.state :as state]))

(defonce *router (router/create {:home    home.screen/render
                                 :service service.screen/render}
                                :home))

(def app
  (ds/with-theme
    {:hui.button/bg (paint/fill 0xFFFF0000)}
    (router/render {:router *router})))

;; reset current app state on eval of this ns
(reset! state/*app app)

(defn -main
  "Run once on app start, starting the humble app."
  [& args]
  (ui/start-app!
    (reset! state/*window
            (ui/window
              {:title    "Border Collie"
               :bg-color 0xFFFFFFFF}
              state/*app)))
  (state/redraw!))