(ns border-collie.design-system.core
  (:require
    [io.github.humbleui.ui.dynamic :as dynamic]
    [border-collie.design-system.theme :as ds.theme]
    [io.github.humbleui.ui :as ui]))

(defn screen
  [child]
  (ui/padding
    16 16
    child))

(defn button
  [on-click text]
  (ui/button
    on-click
    (ui/height
      24
      (ui/valign
        0.5
        (ui/label text)))))

(defn with-height
  [height child]
  (if height
    (ui/height height child)
    child))

(defn card
  ([child]
   (card {} child))
  ([options child]
   (dynamic/dynamic
     ctx
     [{:material.card/keys [background-color border-color]} ctx]
     (with-height
       (:height options)
       (ui/rounded-rect
         {:radius 6} background-color
         (ui/rounded-rect
           {:radius 6} border-color
           (ui/padding
             8 8
             child)))))))

(defn label
  ([text] (label {:role :body-medium} text))
  ([{:keys [role]} text]
   (dynamic/dynamic ctx
                    [font (-> ctx :material/theme :typography role)]
                    (ui/label
                      {:font font}
                      text))))

(defn with-theme
  [child]
  (ds.theme/with-theme child))
