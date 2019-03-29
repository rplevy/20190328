(ns assignment.query
  (:require [clojure.string :as str]
            [assignment.from :as from]
            [assignment.where :as where]))

(defmulti compile (fn [tables where-fields where-macros [operator & args]]
                    operator))

(defmethod compile "query" [from-tables where-fields where-macros
                            [_ [from from-id] [where where-clause]]]
  (cond (or (not= from "from") (not from-id))
        (throw (Exception. "expected: from clause"))

        (or (not= where "where") (not where-clause))
        (throw (Exception. "expected: where clause"))

        :else (format "SELECT * FROM %s WHERE %s"
                      (from/compile from-tables from-id)
                      (where/compile where-fields where-macros where-clause))))

(defmethod compile :default [tables where-fields where-macros where-clause]
  (format "SELECT * FROM data WHERE %s"
          (where/compile where-fields where-macros where-clause)))
