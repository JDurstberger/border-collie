(ns border-collie.settings.screen
  (:require [border-collie.design-system.core :as ds]
            [border-collie.settings.commands :as commands]
            [io.github.humbleui.core :as core]
            [io.github.humbleui.ui :as ui]))

(defonce *services-path-text-field (atom {:text ""}))
(defonce *ignore-services-text-field (atom {:text ""}))

(defn on-render
  [*state]
  (let [configuration (:configuration @*state)]
    (swap! *services-path-text-field assoc :text (:services-path configuration))
    (swap! *ignore-services-text-field assoc :text (:ignore-services configuration))))

(defn create-on-save
  [*state]
  (fn []
    (commands/save *state
                   (-> @*services-path-text-field :text)
                   (-> @*ignore-services-text-field :text))))

(defn render
  [{:keys [*state]}]
  (on-render *state)
  (ds/screen
    (ui/focus-controller
      (ui/column
        (ui/padding 8 8 8 16
                    (ds/label {:role :headline-medium} "Settings"))
        (ds/label {:role :body-small} "Services Directory")
        (ui/gap 0 8)
        (ui/text-field {:focused (core/now)} *services-path-text-field)
        (ui/gap 0 16)
        (ds/label {:role :body-small} "Ignore Services (comma separated)")
        (ui/gap 0 8)
        (ui/text-field *ignore-services-text-field)
        (ui/gap 0 16)
        (ds/button (create-on-save *state) "Save")))))

