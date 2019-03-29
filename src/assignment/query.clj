(ns assignment.query
  (:require [clojure.string :as str]
            [assignment.from :as from]
            [assignment.where :as where]))

(defmulti transpile (fn [tables fields macros [operator & args]]
                    operator))

(defmethod transpile "query" [tables fields macros
                            [_ [from from-id] [where where-clause]]]
  (cond (or (not= from "from") (not from-id))
        (throw (Exception. "expected: from clause"))

        (or (not= where "where") (not where-clause))
        (throw (Exception. "expected: where clause"))

        :else (format "SELECT * FROM %s WHERE %s"
                      (from/transpile tables from-id)
                      (where/transpile fields macros where-clause))))

(defmethod transpile :default [tables fields macros where-clause]
  (format "SELECT * FROM data WHERE %s"
          (where/transpile fields macros where-clause)))
