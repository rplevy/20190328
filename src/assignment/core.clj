(ns assignment.core
  (:require [assignment.where :as where]))

(defn generate-sql
  "fields example:
     {1 :field, 2 :another-field, ...}
   query examples:
     [\"AND\" [> [\"field\" 1] [\"field\" 2]] [= [\"field\" 2] 0]]
     [\"OR\" [> [\"field\" 1] [\"field\" 2]] [= [\"field\" 2] 0]]
     [\"!=\" [\"field\" 2] 0]
  "
  [fields query]
  (format "SELECT * FROM data WHERE %s"
          (where/compile fields query)))
