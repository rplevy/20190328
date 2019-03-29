(ns assignment.select
  (:require [clojure.string :as str]
            [assignment.field :as field]))

(defn transpile [fields field-clauses]
  (str/join ", " (map (partial field/transpile fields) field-clauses)))
