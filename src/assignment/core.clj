(ns assignment.core
  (:require [assignment.where :as where]))

(defn generate-sql
  "fields example 1:
     {:where-fields {1 :field, 2 :another-field, ...}}
  fields example 2:
     {:where-fields {1 :field, 2 :another-field, ...}
      :where-macros {\"is_joe\": [\"=\" [\"field\" 2] \"joe\"]}}
   query examples:
     [\"AND\" [> [\"field\" 1] [\"field\" 2]] [= [\"field\" 2] 0]]
     [\"OR\" [> [\"field\" 1] [\"field\" 2]] [= [\"field\" 2] 0]]
     [\"!=\" [\"field\" 2] 0]
  "
  [{:keys [where-fields where-macros]} query]
  (format "SELECT * FROM data WHERE %s"
          (where/compile where-fields where-macros query)))
