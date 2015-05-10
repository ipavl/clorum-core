(defproject clorum-core "0.1.0-SNAPSHOT"
  :description "Library for message-board style web applications."
  :url "https://github.com/ipavl/clorum-core"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [java-jdbc/dsl "0.1.2"]
                 [lib-noir "0.9.5"]]
  :plugins [[codox "0.8.10"]]
  :codox {:src-dir-uri "https://github.com/ipavl/clorum-core/blob/master/"
          :src-linenum-anchor-prefix "L"})
