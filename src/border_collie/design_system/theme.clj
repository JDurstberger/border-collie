(ns border-collie.design-system.theme
  (:require [io.github.humbleui.font :as font]
            [io.github.humbleui.paint :as paint]
            [io.github.humbleui.typeface :as typeface]
            [io.github.humbleui.ui :as ui]
            [border-collie.design-system.color :as ds.color]))

(defn create-card-theme
  [theme]
  {:material.card/background-color (paint/fill (-> theme :colors :surface))
   :material.card/border-color     (paint/stroke (-> theme :colors :border-color) 4)})

(defn create-default-theme
  [scale]
  (let [font (typeface/make-from-resource "border_collie/design_system/fonts/Roboto-Regular.ttf")]
    {:colors     {:primary      0xFF28A456
                  :background   0xFFFFFFFF
                  :surface      0xFFFFFFFF
                  :border-color 0xFF333333}

     :typography {:font        font
                  :body-large  (font/make-with-cap-height font (float (* scale 16)))
                  :body-medium (font/make-with-cap-height font (float (* scale 14)))
                  :body-small  (font/make-with-cap-height font (float (* scale 12)))}}))

(defn create-hui-button-theme
  [theme]
  {:hui.button/bg         (paint/fill (-> theme :colors :primary))
   :hui.button/bg-hovered (paint/fill (-> theme :colors :primary))
   :hui.button/bg-active  (paint/fill (ds.color/adjust-color (-> theme :colors :primary) -20))})

(defn with-theme
  [child]
  (ui/with-scale
    scale
    (ui/default-theme
      (let [default-theme (create-default-theme scale)
            theme (merge {:material/theme default-theme}
                         (create-hui-button-theme default-theme)
                         (create-card-theme default-theme))]
        theme)
      child)))