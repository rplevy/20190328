(ns assignment.where)

(defmulti compile (fn [fields [operator & args]]
                    operator))

(defmethod compile "AND" [fields [_ & clauses]]
  )

(defmethod compile "OR" [fields [_ & clauses]]
  )

(defmethod compile ">" [fields [_ value-a value-b]]
  )

(defmethod compile "<" [fields [_ value-a value-b]]
  )

(defmethod compile "=" [fields [_ value-a value-b]]
  )

(defmethod compile "!=" [fields [_ value-a value-b]]
  )

(defmethod compile "is_empty" [fields [_ value]]
  )

(defmethod compile "not_empty" [fields [_ value]]
  )
