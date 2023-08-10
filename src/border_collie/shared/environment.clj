(ns border-collie.shared.environment)

(defn get-variable
  [name]
  (System/getenv name))
