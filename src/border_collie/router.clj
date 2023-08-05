(ns border-collie.router
  (:require [io.github.humbleui.ui :as ui]))

(defn create
  [routes default-route]
  (atom {:active-route default-route
         :routes       routes}))

(defn navigate-to
  [*router route]
  (swap! *router assoc :active-route route))

(defn render
  [{:keys [*router] :as dependencies}]
  (ui/dynamic
    _
    [router @*router]
    (if-let [screen-fn ((:active-route router) (:routes router))]
      (screen-fn dependencies)
      (throw (ex-info "unknown route" {:route (:route router)})))))
