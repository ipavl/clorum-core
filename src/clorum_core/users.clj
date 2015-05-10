(ns clorum-core.users
  (:refer-clojure :exclude [get])
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]
            [clorum-core.util.security :as security]
            [clorum-core.util.sanitization :as sanitize]))

(defn all
  "Returns all rows in the users table."
  [db]
  (jdbc/query db
              (sql/select * :users)))

(defn get
  "Returns the user with the specified id."
  [db id]
  (first (jdbc/query db
                     (sql/select * :users (sql/where {:id (sanitize/parse-int id)})))))

(defn get-by-name
  "Returns the user with the specified name."
  [db username]
  (first (jdbc/query db
                     (sql/select * :users (sql/where {:username (first username)})))))

(defn get-replies
  "Returns all replies made by the user (ignores non-verified entries)."
  [db author]
  (jdbc/query db
              (sql/select * :replies (sql/where {:author author :verified true}))))

(defn get-discussions
  "Returns all discussions started by the user (ignores non-verified entries)."
  [db author]
  (jdbc/query db
              (sql/select * :discussions (sql/where {:author author :verified true}))))

(defn create
  "Inserts a new user with the passed parameters."
  [db params]
  (if (nil? (get-by-name db [(:username params)]))
    (jdbc/insert! db :users (merge params {:password (security/encrypt (:password params))}))))

(defn save
  "Updates the user with the specified id with the passed parameters if the given password is correct."
  [db id params]
  (if (security/encrypt-verify (:currentpass params) (:password (get db id)))
    (jdbc/update! db :users (merge (apply dissoc params [:currentpass
                                                         :permissions
                                                         :registered
                                                         :ipaddress
                                                         :username
                                                         :id])
                                                 {:email (sanitize/blank-string (:email params))
                                                  :password (security/encrypt (:password params))}) (sql/where {:id (sanitize/parse-int id)}))))

(defn save-admin
  "Updates the user with the specified id with the passed parameters without verifying the user."
  [db id params]
  (jdbc/update! db :users params (sql/where {:id (sanitize/parse-int id)})))

(defn delete
  "Deletes the user with the specified id."
  [db id]
  (jdbc/delete! db :users (sql/where {:id (sanitize/parse-int id)})))
