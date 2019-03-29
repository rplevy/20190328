(ns assignment.query
  (:require [clojure.string :as str]
            [assignment.from :as from]
            [assignment.select :as select]
            [assignment.where :as where]))

(defmulti transpile (fn [tables fields macros [operator & args]]
                      operator))

(defmethod transpile "query" [tables fields macros
                              [_ & clauses]]
  (if (= 2 (count clauses))
    (let [[[from from-id]
           [where where-clause]] clauses]
      (cond (or (not= from "from") (not from-id))
            (throw (Exception. "expected: from clause"))

            (or (not= where "where") (not where-clause))
            (throw (Exception. "expected: where clause"))

            :else (format "SELECT * FROM %s WHERE %s"
                          (from/transpile tables from-id)
                          (where/transpile fields macros where-clause))))
    (let [[[select & field-clauses]
           [from from-id]
           [where where-clause]] clauses]
      (cond (or (not= select "select") (not field-clauses))
            (throw (Exception. "expected: select clause"))

            (or (not= from "from") (not from-id))
            (throw (Exception. "expected: from clause"))

            (or (not= where "where") (not where-clause))
            (throw (Exception. "expected: where clause"))

            :else (format "SELECT %s FROM %s WHERE %s"
                          (select/transpile fields field-clauses)
                          (from/transpile tables from-id)
                          (where/transpile fields macros where-clause))))))

(defmethod transpile :default [tables fields macros where-clause]
  (format "SELECT * FROM data WHERE %s"
          (where/transpile fields macros where-clause)))
