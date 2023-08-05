(ns border-collie.design-system.theme
  (:require [io.github.humbleui.paint :as paint]
            [io.github.humbleui.ui :as ui]
            [border-collie.design-system.color :as ds.color]) )

(defn create-card-theme
  [theme]
  {:material.card/background-color (paint/fill (-> theme :colors :surface))
   :material.card/border-color     (paint/stroke (-> theme :colors :border-color) 4)})

(def default-theme
  {:colors {:primary      0xFF28A456
            :background   0xFFFFFFFF
            :surface      0xFFFFFFFF
            :border-color 0xFF333333}})

(defn create-hui-button-theme
  [theme]
  {
   :hui.button/bg         (paint/fill (-> theme :colors :primary))
   :hui.button/bg-hovered (paint/fill (-> theme :colors :primary))
   :hui.button/bg-active  (paint/fill (ds.color/adjust-color (-> theme :colors :primary) -20))})

(defn with-theme
  [theme child]
  (let [theme (merge default-theme theme)]
    (ui/default-theme
      (merge {:material/theme theme}
             (create-hui-button-theme theme)
             (create-card-theme theme))
      child)))
