(ns clorum-core.discussions
  (:refer-clojure :exclude [get])
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]
            [clorum-core.util.time :as time]
            [clorum-core.util.security :as security]
            [clorum-core.util.sanitization :as sanitize]
            [clorum-core.users :as users-model]))

(defn all
  "Returns all rows in the discussions table."
  [db]
  (jdbc/query db
              (sql/select * :discussions)))

(defn get
  "Returns the discussion with the specified id."
  [db id]
  (first (jdbc/query db
                     (sql/select * :discussions (sql/where {:id id})))))

(defn get-replies
  "Returns all rows in the replies table with the specified parent id."
  [db parent]
  (jdbc/query db
              (sql/select * :replies (sql/where {:parent parent}))))

(defn get-recent
  "Returns the newest n discussions."
  [db n]
  (jdbc/query db
              [(clojure.string/join ["SELECT * FROM discussions ORDER BY id DESC LIMIT " n])]))

(defn create
  "Inserts a new discussion with the passed parameters, sanitizing blank author and category fields."
  [db params]
  (def db-user (users-model/get-by-name db [(:author params)]))
  (if db-user
    (def verified? (security/encrypt-verify (:password params) (:password db-user)))
    (def verified? false))
  (def insert-time time/current-time-sql)

  (jdbc/insert! db :discussions (merge (apply dissoc params [:password])
                                              {:author (sanitize/author (:author params))
                                               :category (sanitize/category (:category params))
                                               :created (insert-time)
                                               :modified (insert-time)
                                               :verified verified?}))
  ;; Rough way of getting the inserted thread's ID. Should probably also check time.
  (last (jdbc/query db
                     (sql/select :id :discussions (sql/where {:author (sanitize/author (:author params))
                                                              :category (sanitize/category (:category params))
                                                              :verified verified?})))))

(defn create-reply
  "Inserts a new reply with the passed parameters, sanitizing blank author fields."
  [db params]
  (def db-user (users-model/get-by-name db [(:author params)]))
  (if db-user
    (def verified? (security/encrypt-verify (:password params) (:password db-user)))
    (def verified? false))

  (jdbc/insert! db :replies (merge (apply dissoc params [:password])
                                              {:author (sanitize/author (:author params))
                                               :created (time/current-time-sql)
                                               :modified (time/current-time-sql)
                                               :verified verified?})))

(defn save
  "Updates the discussion with the specified id with the passed parameters."
  [db id params]
  (jdbc/update! db :discussions params (sql/where {:id id})))

(defn delete
  "Deletes the discussion with the specified id along with its child replies."
  [db id]
  (jdbc/delete! db :discussions (sql/where {:id id}))
  (jdbc/delete! db :replies (sql/where {:parent id})))
