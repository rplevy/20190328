(ns assignment.from)

(defn transpile [tables from-id]
  (if-let [table (get tables from-id)]
    (name table)
    (throw (Exception. "undefined table"))))
