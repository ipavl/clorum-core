(defproject clorum-core "0.1.0-SNAPSHOT"
  :description "Core library for Clorum"
  :url "https://github.com/ipavl/clorum-core"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [java-jdbc/dsl "0.1.1"]
                 [mysql/mysql-connector-java "5.1.34"]
                 [clj-time "0.9.0"]
                 [lib-noir "0.9.5"]]
  :plugins [[codox "0.8.10"]]
  :codox {:src-dir-uri "https://github.com/ipavl/clorum-core/blob/master/"
          :src-linenum-anchor-prefix "L"})
