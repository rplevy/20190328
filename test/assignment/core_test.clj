(ns assignment.core-test
  (:require [clojure.test :refer :all]
            [assignment.core :as base]))

(deftest test-wheres
  (let [fields {:where-fields {1 :id
                               2 :name
                               3 :date_joined
                               4 :age}}]
    (is (= (base/generate-sql fields ["is_empty" ["field" 3]])
           "SELECT * FROM data WHERE date_joined IS NULL"))
    (is (= (base/generate-sql fields ["=" ["field" 3] nil])
           "SELECT * FROM data WHERE date_joined IS NULL"))
    (is (= (base/generate-sql fields [">" ["field" 4] 35])
           "SELECT * FROM data WHERE age > 35"))
    (is (= (base/generate-sql fields ["AND"
                                      ["<" ["field" 1]  5]
                                      ["=" ["field" 2] "joe"]])
           "SELECT * FROM data WHERE id < 5 AND name = 'joe'"))
    (is (= (base/generate-sql fields ["OR"
                                      ["!=" ["field" 3] "2015-11-01"]
                                      ["=" ["field" 1] 456]])
           "SELECT * FROM data WHERE date_joined <> '2015-11-01' OR id = 456"))
    (is (= (base/generate-sql fields ["AND"
                                      ["!=" ["field" 3] nil]
                                      ["OR"
                                       [">" ["field" 4] 25]
                                       ["=" ["field" 2] "Jerry"]]])
           (str "SELECT * FROM data WHERE date_joined IS NOT NULL AND "
                "(age > 25 OR name = 'Jerry')")))))

(deftest test-macros
  (let [fields {:where-fields {1 :id
                               2 :name
                               3 :date_joined
                               4 :age}
                :where-macros {"is_joe" ["=" ["field" 2] "joe"]}}]
    (is (= (base/generate-sql fields ["AND"
                                      ["<" ["field" 1] 5]
                                      ["macro" "is_joe"]])
           "SELECT * FROM data WHERE id < 5 AND name = 'joe'"))))

(deftest test-from
  (let [query-spec {:where-fields {1 :id
                                   2 :name
                                   3 :date_joined
                                   4 :age}
                    :where-macros {"is_joe" ["=" ["field" 2] "joe"]}
                    :from-tables {1 :stuff
                                  2 :things}}]
    (is (= (base/generate-sql query-spec
                              ["query"
                               ["from" 1]
                               ["where"
                                ["AND"
                                 ["<" ["field" 1] 5]
                                 ["macro" "is_joe"]]]])
           "SELECT * FROM stuff WHERE id < 5 AND name = 'joe'"))
    (is (= (base/generate-sql query-spec
                              ["query"
                               ["from" 2]
                               ["where"
                                ["AND"
                                 ["<" ["field" 1] 5]
                                 ["macro" "is_joe"]]]])
           "SELECT * FROM things WHERE id < 5 AND name = 'joe'"))))
