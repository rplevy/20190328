(ns assignment.field)

(defmulti compile (fn [fields value]
                    (if (vector? value)
                      (first value)
                      "value")))

(defmethod compile "field" [fields [_ field-id]]
  (name (get fields field-id)))

(defmethod compile "value" [_ value]
  (if (number? value)
    value
    (format "'%s'" value)))

(defmethod compile :default [_ _]
  (throw (Exception. "unknown operator. expected: field or value.")))
