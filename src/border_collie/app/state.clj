(ns border-collie.app.state)

(def *state (atom {}))

(defn init
  [configuration]
  (reset! *state {:configuration configuration
                  :services      {}
                  :service-tasks {}}))