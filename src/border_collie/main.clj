(ns border-collie.main
  (:require [io.github.humbleui.ui :as ui]
            [border-collie.router :as router]
            [border-collie.home.screen :as home.screen]
            [border-collie.service.screen :as service.screen]
            [border-collie.state :as state]))

(defonce *router (router/create :home {:home    home.screen/render
                                       :service service.screen/render}))

(def app
  (ui/default-theme
    {}
    (ui/column
      (ui/padding
        10 10
        (ui/halign
          0.5
          (ui/label {:bg-color 0xFF0000FF} "Border Collie")))
      (router/render {:router *router}))))

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