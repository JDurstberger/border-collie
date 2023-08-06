(ns border-collie.home.screen
  (:require
    [border-collie.design-system.core :as ds]
    [border-collie.home.commands :as commands]
    [border-collie.home.service-card :as service-card]
    [io.github.humbleui.ui :as ui]))

(defn on-render
  [*state]
  (commands/load-services *state))

(defn render
  [{:keys [*state] :as dependencies}]
  (on-render *state)
  (ds/screen
    (ui/dynamic
      _
      [services (-> @*state :services (vals))]
      (ui/vscroll
        (ui/column
          (map (partial service-card/create-component dependencies) services))))))
