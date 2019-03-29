(ns assignment.field)

(defmulti compile (fn [fields value]
                    (if (vector? value)
                      (first value)
                      "value")))

(defmethod compile "field" [fields [_ field-id]]
  (name (get fields field-id)))

(defmethod compile "value" [fields value]
  value)

(defmethod compile :default [fields value]
  (throw (Exception. "unknown operator. expected: field or value.")))
