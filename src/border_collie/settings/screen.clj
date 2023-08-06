(ns border-collie.settings.screen
  (:require [border-collie.design-system.core :as ds]))

(defn render
  [_]
  (ds/screen
    (ds/label {:role :body-medium} "Settings")))

