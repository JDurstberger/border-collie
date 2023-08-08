(ns border-collie.main
  (:require [io.github.humbleui.ui :as ui]
            [border-collie.home.commands :as home.commands]
            [border-collie.app.state :as app-state]
            [border-collie.app.core :as app]
            [border-collie.state :as state]
            [border-collie.logs :as logs]))

;; reset current app state on eval of this ns
(reset! state/*app (app/init))

(defn -main
  "Run once on app start, starting the humble app."
  [& args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. ^Runnable (fn []
                                         (home.commands/stop-all-services app-state/*state))))
  (ui/start-app!
    (reset! state/*window
            (ui/window
              {:title    "Border Collie"
               :bg-color 0xFFFFFFFF}
              state/*app)))
  (state/redraw!))