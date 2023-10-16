(ns dev
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clojure.tools.namespace.repl :refer [refresh]]
   [libpython-clj2.python :as py :refer [py. py.. py.-]]))

(when (io/resource "local.clj")
  (load "local"))
