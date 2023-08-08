(ns border-collie.settings.commands
  (:require
    [border-collie.shared.configuration :as configuration]
    [border-collie.app.state :as app-state]))

(defn save
  [*state services-path ignore-services]
  (app-state/swap-configuration! *state #(configuration/update-configuration %
                                                                             {:services-path   services-path
                                                                              :ignore-services ignore-services})))
