(ns lagenorhynque.libpython-clj-trial
  (:gen-class)
  (:require
   [libpython-clj2.python :as py :refer [py. py.. py.-]]
   [libpython-clj2.require :refer [require-python]]))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))

(comment
  (require-python
   '[numpy :as np]
   '[numpy.random :as np.random]
   '[pandas :as pd])

  (def dates (pd/date_range "1/1/2000" :periods 8))
  (def table (pd/DataFrame
              (np.random/randn 8 4)
              :index dates
              :columns ["A" "B" "C" "D"]))
  (def row-date (pd/date_range
                 :start "2000-01-01"
                 :end "2000-01-01"))
  (py/get-item (py.- table :loc) row-date))
