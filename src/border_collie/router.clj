(ns border-collie.router
  (:require [io.github.humbleui.ui :as ui]))

(defn create
  [routes default-route]
  (atom {:active-route default-route
         :routes       routes}))

(defn navigate-to
  [*router route-or-navigation]
  (if (keyword? route-or-navigation)
    (swap! *router assoc :active-route {:route route-or-navigation})
    (swap! *router assoc :active-route route-or-navigation)))

(defn render
  [{:keys [*router] :as dependencies}]
  (ui/dynamic
    _
    [router @*router]
    (if-let [screen-fn ((-> router :active-route :route) (:routes router))]
      (screen-fn dependencies)
      (throw (ex-info "unknown route" {:route (:route router)})))))
