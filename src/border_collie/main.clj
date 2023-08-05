(ns border-collie.main
  (:require [io.github.humbleui.ui :as ui]
            [border-collie.state :as state]))

(defonce *text-field (atom {:text "Start"}))

(defn on-load-click
  []
  (println "CLICK"))

(def app
  "Main app definition."
  (ui/default-theme
    {}
    (ui/center
      (ui/row
        (ui/text-field *text-field)
        (ui/button on-load-click
                   (ui/label "Load"))

        (ui/dynamic _ [text (:text @*text-field)]
                    (ui/label (str text)))))))

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