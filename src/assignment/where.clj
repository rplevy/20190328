(ns assignment.where
  (:require [clojure.string :as str]
            [assignment.field :as field]))

(defmulti compile (fn [fields [operator & args]]
                    operator))

(defn nested? [[operator & _]]
  (#{"AND" "OR"} operator))

(defn join-clauses [fields clauses sep]
  (str/join
   sep
   (map (fn [clause]
          (if (nested? clause)
            (format "(%s)" (compile fields clause))
            (compile fields clause)))
        clauses)))

(defmethod compile "AND" [fields [_ & clauses]]
  (join-clauses fields clauses " AND "))

(defmethod compile "OR" [fields [_ & clauses]]
  (join-clauses fields clauses " OR "))

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
    (format "%s = %s"
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
