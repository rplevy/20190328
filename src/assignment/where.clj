(ns assignment.where
  (:require [clojure.string :as str]
            [assignment.field :as field]))

(defmulti transpile (fn [fields macros [operator & args]]
                    operator))

(defmethod transpile "macro" [fields macros [_ macro-name]]
  (if-let [clause (get macros macro-name)]
    (transpile fields macros clause)
    (throw (Exception. "undefined macro"))))

(defn nested? [[operator & _]]
  (#{"AND" "OR"} operator))

(defn join-clauses [fields macros clauses sep]
  (str/join
   sep
   (map (fn [clause]
          (if (nested? clause)
            (format "(%s)" (transpile fields macros clause))
            (transpile fields macros clause)))
        clauses)))

(defmethod transpile "AND" [fields macros [_ & clauses]]
  (join-clauses fields macros clauses " AND "))

(defmethod transpile "OR" [fields macros [_ & clauses]]
  (join-clauses fields macros clauses " OR "))

(defmethod transpile ">" [fields _ [_ value-a value-b]]
  (format "%s > %s"
          (field/transpile fields value-a)
          (field/transpile fields value-b)))

(defmethod transpile "<" [fields _ [_ value-a value-b]]
  (format "%s < %s"
          (field/transpile fields value-a)
          (field/transpile fields value-b)))

(defmethod transpile "=" [fields _ [_ value-a value-b]]
  (if (nil? value-b)
    (format "%s IS NULL"
            (field/transpile fields value-a))
    (format "%s = %s"
            (field/transpile fields value-a)
            (field/transpile fields value-b))))

(defmethod transpile "!=" [fields _ [_ value-a value-b]]
  (if (nil? value-b)
    (format "%s IS NOT NULL"
            (field/transpile fields value-a))
    (format "%s <> %s"
            (field/transpile fields value-a)
            (field/transpile fields value-b))))

(defmethod transpile "is_empty" [fields _ [_ value]]
  (format "%s IS NULL" (field/transpile fields value)))

(defmethod transpile "not_empty" [fields _ [_ value]]
  (format "%s IS NOT NULL" (field/transpile fields value)))

(defmethod transpile :default [fields _ value]
  (throw (Exception. "unknown operator. expected: operator for WHERE clause.")))
