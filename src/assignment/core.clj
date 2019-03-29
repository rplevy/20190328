(ns assignment.core
  (:require [assignment.query :as query]))

(defn generate-sql
  [{:keys [where-fields where-macros from-tables]} query]
  (query/compile from-tables where-fields where-macros query))
