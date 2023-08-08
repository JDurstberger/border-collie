(ns border-collie.home.screen
  (:require
    [border-collie.design-system.core :as ds]
    [border-collie.home.commands :as commands]
    [border-collie.home.service-card :as service-card]
    [io.github.humbleui.ui :as ui]))

(defn on-render
  [*state]
  (println @*state)
  (commands/load-services *state))

(defn render
  [{:keys [*state] :as dependencies}]
  (on-render *state)
  (ds/screen
    (ui/dynamic
      _
      [services (-> @*state :services (vals))]
      (if (empty? services)
        (ui/center
          (ds/label {:role :headline-medium} "No services found."))
        (ui/vscroll
          (ui/column
            (map (partial service-card/create-component dependencies) services)))))))
