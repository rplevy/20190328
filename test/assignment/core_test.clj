(ns assignment.core-test
  (:require [clojure.test :refer :all]
            [assignment.core :as base]))

(deftest test-generate-sql
  (let [fields {1 :id
                2 :name
                3 :date_joined
                4 :age}]
    (is (= (base/generate-sql fields ["is_empty" ["field" 3]])
           "SELECT * FROM data WHERE date_joined IS NULL"))
    (is (= (base/generate-sql fields ["=" ["field" 3] nil])
           "SELECT * FROM data WHERE date_joined IS NULL"))
    (is (= (base/generate-sql fields [">" ["field" 4] 35])
           "SELECT * FROM data WHERE age > 35"))
    (is (= (base/generate-sql fields ["AND"
                                 ["<" ["field" 1]  5]
                                 ["=" ["field" 2] "joe"]])
           "SELECT * FROM data WHERE id < 5 AND name='joe'"))
    (is (= (base/generate-sql fields ["OR"
                                 ["!=" ["field" 3] "2015-11-01"]
                                 ["=" ["field" 1] 456]])
           "SELECT * FROM data WHERE date_joined <> '2015-11-01' or id = 456"))
    (is (= (base/generate-sql fields ["AND"
                                 ["!=" ["field" 3] nil]
                                 ["OR"
                                  [">" ["field" 4] 25]
                                  ["=" ["field" 2] "Jerry"]]])
           (str "SELECT * FROM data WHERE date_joined IS NOT NULL AND "
                "(age > 25 OR name = 'Jerry')")))))
