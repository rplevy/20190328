(ns assignment.field)

(defmulti transpile (fn [fields value]
                    (if (vector? value)
                      (first value)
                      "value")))

(defmethod transpile "field" [fields [_ field-id]]
  (name (get fields field-id)))

(defmethod transpile "value" [_ value]
  (if (number? value)
    value
    (format "'%s'" value)))

(defmethod transpile :default [_ _]
  (throw (Exception. "unknown operator. expected: field or value.")))
