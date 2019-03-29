# assignment

## Usage

The DSL supports a few different kinds of input:

1. the where-only form:

```clojure
(generate-sql
  {:fields {1 :id, 2 :name}}
  ["<" ["field" 1] 5]) ; => SELECT * FROM data WHERE id < 5
```

2. macros:

```clojure
(generate-sql
  {:fields {1 :id, 2 :name}
   :macros {"is_joe" ["=" ["field" 2] "joe"]}}
  ["AND"
    ["<" ["field" 1] 5]
    ["macro" "is_joe"]]) ; => SELECT * FROM data WHERE id < 5 AND name = 'joe'
```

3. a query expression with from clause

```clojure
(generate-sql
  {:fields {1 :id
            2 :name
            3 :date_joined
            4 :age}
   :macros {"is_joe" ["=" ["field" 2] "joe"]}
   :tables {1 :stuff
            2 :things}}
  ["query"
    ["from" 1]
    ["where"
      ["AND"
        ["<" ["field" 1] 5]
        ["macro" "is_joe"]]]]) ; => SELECT * FROM stuff WHERE id < 5 AND name = 'joe'
```

4. a full query expression with where, from, and select clauses

```clojure
(generate-sql
  {:fields {1 :id
            2 :name
            3 :date_joined
            4 :age}
   :macros {"is_joe" ["=" ["field" 2] "joe"]}
   :tables {1 :stuff
            2 :things}}
  ["query"
    ["select" ["field" 1] ["field" 2]]
    ["from" 1]
    ["where"
      ["AND"
        ["<" ["field" 1] 5]
        ["macro" "is_joe"]]]]) ; => SELECT id, name FROM stuff WHERE id < 5 AND name = 'joe'
```

## Tests

```lein test```
