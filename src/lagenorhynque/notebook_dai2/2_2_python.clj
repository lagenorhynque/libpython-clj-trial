(ns lagenorhynque.notebook-dai2.2-2-python
  "『Pythonによるあたらしいデータ分析の教科書 第2版』
  https://www.shoeisha.co.jp/book/detail/9784798178776
  Chapter2 Pythonと環境
  2.2 Pythonの基礎"
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [libpython-clj2.python :as py :refer [py. py.. py.-]]
   [libpython-clj2.require :refer [require-python]]))

(comment
  ;;;; 2.2.1 Pythonの文法

  (doseq [i (range 10)]
    (cond
      (zero? (mod i 5)) (println "ham")
      (zero? (mod i 3)) (println "eggs")
      :else (println "spam")))
  (println "Finish!")

  (require-python
   'sys
   'os)

  ;;;; 2.2.2 基本構文

  ;;; 条件分岐と繰り返し

  (doseq [year [1950 2000 2020]]
    (cond
      (< year 1989) (println "昭和")
      (< year 2019) (println "平成")
      :else (println "令和")))

  ;;; 例外処理

  (try
    (/ 1 0)
    (catch ArithmeticException _
      (println "0で割れません")))

  (let [names ["spam" "ham" "eggs"]]
    (loop [lens (transient [])
           [name & names] names]
      (if name
        (recur (conj! lens (count name))
               names)
        (persistent! lens))))

  ;;; 内包表記

  ;; シーケンス内包表記
  (let [names ["spam" "ham" "eggs"]]
    (for [name names]
      (count name)))

  ;; ベクター
  (let [names ["spam" "ham" "eggs"]]
    (into [] (map count) names))

  ;; セット
  (let [names ["spam" "ham" "eggs"]]
    (into #{} (map count) names))

  ;; マップ
  (let [names ["spam" "ham" "eggs"]]
    (into {} (map (fn [name] [name (count name)])) names))

  ;; 条件付きのシーケンス内包表記
  (for [x (range 10)
        :when (zero? (mod x 2))]
    (* x x))

  ;; ネストしたシーケンス内包表記
  (for [y (range 3)]
    (for [x (range 10)
          :when (zero? (mod x 2))]
      [y (* x x)]))

  ;;; ジェネレーター式

  ;; ベクター
  (let [l (mapv #(* % %) (range 100000))]
    [(type l) (count l)])

  ;; 遅延シーケンス
  (let [l (map #(* % %) (range 100000))]
    [(type l) (count l)])

  ;;; ファイル入出力

  (with-open [f (io/writer "sample.txt")]
    (.write f "こんにちは\n")
    (.write f "Python\n"))

  (with-open [f (io/reader "sample.txt")]
    (doall (line-seq f)))

  ;;; 文字列操作

  (let [s1 "hello python"]
    [(str/upper-case s1)
     (str/lower-case s1)
     (str/replace s1 #"\b." str/upper-case)
     (str/replace s1 "hello" "Hi")])

  (let [s2 "    spam  ham    eggs    "]
    [(-> s2
         str/trim
         (str/split #"\s+"))
     (str/trim s2)])

  (let [s3 "sample.jpg"]
    (some? (re-find #"(?:jpg|gif|png)" s3)))

  (every? #(Character/isDigit %) "123456789")

  (let [s1 "hello python"]
    (count s1))

  (let [s1 "hello python"]
    (str/includes? s1 "py"))

  (str/join "-" ["spam" "ham" "eggs"])

  (let [name "takanory"
        lang "python"]
    (format "%sは%sが好きです" name lang))

  (let [name "takanory"
        lang "python"]
    (str
     (str/capitalize name)
     "は"
     (str/upper-case lang)
     "が好きです"))

  (defmacro ? [v]
    `(let [x# ~v]
       (str '~v \= (pr-str x#))))

  (let [name "takanory"
        lang "python"]
    (str
     (? name)
     "は"
     (? lang)
     "が好きです"))

  ;;;; 2.2.3 標準ライブラリ

  ;;; reモジュール

  (require-python 're)

  (let [prog (re/compile "(P(yth|l)|Z)o[pn]e?")]
    (py. prog search "Python"))

  (let [prog (re/compile "(P(yth|l)|Z)o[pn]e?")]
    (py. prog search "Spam"))

  ;;; loggingモジュール

  (require-python 'logging)

  (logging/basicConfig
   :filename "example.log"
   :level logging/INFO
   :format "%(asctime)s:%(levelname)s:%(message)s")

  (logging/debug "デバッグレベル")

  (logging/info "INFOレベル")

  (logging/warning "警告レベル")

  (logging/error "エラーレベル")

  (logging/critical "重大なエラー")

  ;;; datetimeモジュール

  (require-python
   '[datetime :refer [date]]
   '[datetime.datetime :as datetime]
   '[datetime.date :as date])

  (datetime/now)

  (date/today)

  (py. (date/today) __sub__ (date 2008 12 3))

  (py. (datetime/now) isoformat)

  (py. (date/today) strftime "%Y年%m月%d日")

  (datetime/strptime "2022年05月02日" "%Y年%m月%d日")

  ;;; pickleモジュール

  (require-python
   'pickle
   '[io :as py.io])

  (let [d {"today" (date/today)
           "delta" (py. (date 2023 1 1) __sub__ (date/today))}]
    (pickle/dumps d))

  (let [d {"today" (date/today)
           "delta" (py. (date 2023 1 1) __sub__ (date/today))}
        f (py.io/open "data.pkl" "wb")]
    (pickle/dump d f)
    (py. f close))

  (let [f (py.io/open "data.pkl" "rb")
        new-d (pickle/load f)]
    (py. f close)
    new-d)

  ;;; pathlibモジュール

  (require-python
   '[pathlib :refer [Path]])

  (let [p (Path)]
    p)

  (let [p (Path)]
    (doseq [filepath (py. p glob "*.txt")]
      (-> filepath
          (py. read_text :encoding "utf-8")
          println)))

  (let [p (Path "/spam")]
    (py.. p (__truediv__ "ham") (__truediv__ "eggs.txt")))

  (let [p (Path "date.pkl")]
    (py. p exists))

  (let [p (Path "date.pkl")]
    (py. p is_dir))

  )
