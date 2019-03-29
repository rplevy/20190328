(ns assignment.where
  (:require [assignment.field :as field]))

(defmulti compile (fn [fields [operator & args]]
                    operator))

(defmethod compile "AND" [fields [_ & clauses]]
  )

(defmethod compile "OR" [fields [_ & clauses]]
  )

(defmethod compile ">" [fields [_ value-a value-b]]
  (format "%s > %s"
          (field/compile fields value-a)
          (field/compile fields value-b)))

(defmethod compile "<" [fields [_ value-a value-b]]
  (format "%s < %s"
          (field/compile fields value-a)
          (field/compile fields value-b)))

(defmethod compile "=" [fields [_ value-a value-b]]
  (if (nil? value-b)
    (format "%s IS NULL"
            (field/compile fields value-a))
    (format "%s <> %s"
            (field/compile fields value-a)
            (field/compile fields value-b))))

(defmethod compile "!=" [fields [_ value-a value-b]]
  (if (nil? value-b)
    (format "%s IS NOT NULL"
            (field/compile fields value-a))
    (format "%s <> %s"
            (field/compile fields value-a)
            (field/compile fields value-b))))

(defmethod compile "is_empty" [fields [_ value]]
  (format "%s IS NULL" (field/compile fields value)))

(defmethod compile "not_empty" [fields [_ value]]
  (format "%s IS NOT NULL" (field/compile fields value)))

(defmethod compile :default [fields value]
  (throw (Exception. "unknown operator. expected: operator for WHERE clause.")))
