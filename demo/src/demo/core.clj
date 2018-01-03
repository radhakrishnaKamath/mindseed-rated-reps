(ns demo.core
  (:require [postal.core :as p]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:gen-class))

(defn sendmail [to cc title content attachment-path & [defer?]]
  (let [from "aanjal.joshi@gmail.com"
        server {:host "smtp.gmail.com"
                :sender from
                :user from
                :pass "01studyleague"
                :ssl true
                :port 465}
        msg {:from from
             :reply-to from
             :to to
             :cc cc
             :subject title
             :body (if attachment-path
                     [{:type "text/html"
                       :content content}
                      {:type :attachment
                       :content (java.io.File. attachment-path)}]
                     [{:type "text/html"
                       :content content}])}]
    (if defer?      (future (p/send-message server msg))
        (p/send-message server msg))))

(defn read-csv [] (with-open [reader (io/reader "/home/radhakrishna/demo/resources/mock.csv")]
                (doall
                 (csv/read-csv reader))))

(defn write-csv [] (with-open [writer (io/writer "/home/radhakrishna/demo/resources/out-file.csv")]
                 (csv/write-csv writer
                                [["abc" "def"]
                                 ["ghi" "jkl"]])))
(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))

(csv-data->maps (read-csv))
