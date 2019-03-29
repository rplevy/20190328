(ns assignment.where
  (:require [clojure.string :as str]
            [assignment.field :as field]))

(defmulti compile (fn [fields macros [operator & args]]
                    operator))

(defmethod compile "macro" [fields macros [_ macro-name]]
  (if-let [clause (get macros macro-name)]
    (compile fields macros clause)
    (throw (Exception. "undefined macro"))))

(defn nested? [[operator & _]]
  (#{"AND" "OR"} operator))

(defn join-clauses [fields macros clauses sep]
  (str/join
   sep
   (map (fn [clause]
          (if (nested? clause)
            (format "(%s)" (compile fields macros clause))
            (compile fields macros clause)))
        clauses)))

(defmethod compile "AND" [fields macros [_ & clauses]]
  (join-clauses fields macros clauses " AND "))

(defmethod compile "OR" [fields macros [_ & clauses]]
  (join-clauses fields macros clauses " OR "))

(defmethod compile ">" [fields _ [_ value-a value-b]]
  (format "%s > %s"
          (field/compile fields value-a)
          (field/compile fields value-b)))

(defmethod compile "<" [fields _ [_ value-a value-b]]
  (format "%s < %s"
          (field/compile fields value-a)
          (field/compile fields value-b)))

(defmethod compile "=" [fields _ [_ value-a value-b]]
  (if (nil? value-b)
    (format "%s IS NULL"
            (field/compile fields value-a))
    (format "%s = %s"
            (field/compile fields value-a)
            (field/compile fields value-b))))

(defmethod compile "!=" [fields _ [_ value-a value-b]]
  (if (nil? value-b)
    (format "%s IS NOT NULL"
            (field/compile fields value-a))
    (format "%s <> %s"
            (field/compile fields value-a)
            (field/compile fields value-b))))

(defmethod compile "is_empty" [fields _ [_ value]]
  (format "%s IS NULL" (field/compile fields value)))

(defmethod compile "not_empty" [fields _ [_ value]]
  (format "%s IS NOT NULL" (field/compile fields value)))

(defmethod compile :default [fields _ value]
  (throw (Exception. "unknown operator. expected: operator for WHERE clause.")))
