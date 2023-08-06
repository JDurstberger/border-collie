(ns border-collie.settings.commands
  (:require
    [border-collie.shared.configuration :as configuration]))

(defn save
  [*state services-path ignore-services]
  (swap! *state update :configuration merge {:services-path   services-path
                                             :ignore-services ignore-services})
  (configuration/write-to-file (:configuration @*state)))