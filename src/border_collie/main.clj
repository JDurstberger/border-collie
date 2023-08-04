(ns border_collie.main
  (:require [io.github.humbleui.ui :as ui]))


(def ui
  (ui/default-theme {}
                    (ui/center
                      (ui/label "Hello from Humble UI! 👋"))))

(defn -main [& _args]
  (ui/start-app!
    (ui/window
      {:title "Humble 🐝 UI"}
      #'ui)))