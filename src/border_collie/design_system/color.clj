(ns border-collie.design-system.color
  (:require [clojure.string :as string]))

(defn- adjust-color-part
  [percentage hex]
  (-> (read-string (str "0x" hex))
      (* (/ (+ 100 percentage) 100))
      (Integer/toHexString)))

(defn adjust-color
  [color percentage]
  (let [hex-string (Long/toHexString color)
        [_ t & colors] (re-find #"(..)(..)(..)(..)" hex-string)
        colors (map (partial adjust-color-part percentage) colors)
        new-hex-string (str "0x" (string/join (concat [t] colors)))
        numb (read-string new-hex-string)]
    numb))
