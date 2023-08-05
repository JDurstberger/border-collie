(ns border-collie.home.screen
  (:require [border-collie.router :as router]
            [io.github.humbleui.ui :as ui]))

(defn render
  [{:keys [router]}]
  (ui/column
    (ui/label "HOME")
    (ui/button (fn [] (router/navigate-to router :service))
               (ui/label "GO TO SERVICE"))
    ))
