(ns assignment.from)

(defn compile [from-tables from-id]
  (if-let [table (get from-tables from-id)]
    (name table)
    (throw (Exception. "undefined table"))))
