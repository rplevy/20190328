(ns assignment.core
  (:require [assignment.query :as query]))

(defn generate-sql
  [{:keys [fields macros tables]} query]
  (query/transpile tables fields macros query))
