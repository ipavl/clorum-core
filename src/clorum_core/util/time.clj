(ns clorum-core.util.time
  (:require [clj-time.core :as time]
            [clj-time.coerce :as timec]))

(defn current-time-sql
  "Returns the current SQL timestamp."
  []
  (str (timec/to-sql-time (time/now))))
