(ns clorum-core.categories
  (:refer-clojure :exclude [get])
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]))

(defn all
  "Returns all unique categories with the number of discussions ordered from the most to least."
  [db]
  (jdbc/query db
              ["SELECT category, COUNT(1) AS discussions FROM discussions GROUP BY category ORDER BY discussions DESC"]))

(defn get
  "Returns all rows matching the specified category."
  [db category]
  (jdbc/query db
              (sql/select * :discussions (sql/where {:category category}))))
