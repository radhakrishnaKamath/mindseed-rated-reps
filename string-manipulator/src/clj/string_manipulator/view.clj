(ns string-manipulator.view
  (:require [string-manipulator.db.core :as db]
            [string-manipulator.layout :as layout]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [postal.core :as p]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.cron :refer [schedule cron-schedule]]))

(defn get-multipart-param [request name]
  (get-in request [:multuipart-params name]))

(defn add-todo [task-name user]
  (println "@@@" task-name "###" user)
  (db/create-task task-name user)
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

#_(defn add-todo [request]
  (println "@@@" request)
    (let [task-name (get-multipart-param request "task-name")
          user (get-multipart-param request "user")]
      (db/create-task task-name user)
      (layout/render-json (map #(select-keys % [:task-name :status]) (db/get-tasks "rated")))))

(defn check-login [user pass]
  (println "@@@ user" user "### pass" pass)
  (layout/render-json (map #(select-keys % [:user :password]) (db/get-user user pass))))

(defn done-task [task-name user]
  (println "@@@" task-name "###" user)
  (db/update-task task-name user)
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

(defn show-todo [user]
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

(defn upd-task [task-name-old task-name-upd user]
  (db/update-todo task-name-old task-name-upd user)
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

#_(defn read-csv [] (with-open [reader (io/reader "/home/radhakrishna/hamara-game/rkh/resources/docs/data.csv")]
                    (doall
                     (csv/read-csv reader))))

#_(defn read-csv1 [] (with-open [reader (io/reader "/home/radhakrishna/hamara-game/rkh/resources/docs/data.csv")]
                    (doall
                     (csv/read-csv reader))))

#_(defn write-csv [] (with-open [writer (io/writer "/home/radhakrishna/demo/resources/out-file.csv")]
                     (csv/write-csv writer
                                    [["abc" "def"]
                                     ["ghi" "jkl"]])))
#_(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))

#_(defn enter-activity [] (doall (map #(db/create-activity %) (csv-data->maps (read-csv)))))

#_(defn enter-activity [] (doall (map #(db/create-question %) (csv-data->maps (read-csv1)))))

(def email "aanjal.joshi@gmail.com")
(def pass "01studyleague")

(def conn {:host "smtp.gmail.com"
           :ssl true
           :user email
           :pass pass})

(defn welcome [user name]
  (p/send-message conn {:from email
                        :to user
                        :subject "Welcome to RKH"
                        :body (str "Hi " name " Congratulations on starting a new phase of learning!! We the team of RKH wish you all the best")}))

(defjob NoOpJob
  [ctx]
  (println "Did one thing"))

(defjob NoOpJob1
  [ctx]
  (println "Did someother thing"))

(defn cron []
  (let [s   (-> (qs/initialize) qs/start)
        job1 (j/build
              (j/of-type NoOpJob)
              (j/with-identity (j/key "jobs.noop.1")))
        job2 (j/build
              (j/of-type NoOpJob1)
              (j/with-identity (j/key "jobs.noop.2")))
        trigger1 (t/build
                  (t/with-identity (t/key "triggers.1"))
                  (t/start-now)
                  (t/with-schedule (schedule
                                    (cron-schedule "50 * * ? * *"))))
        trigger2 (t/build
                  (t/with-identity (t/key "triggers.2"))
                  (t/start-now)
                  (t/with-schedule (schedule
                                    (cron-schedule "50 * * ? * *"))))]
    (qs/schedule s job1 trigger1)
    (qs/schedule s job2 trigger2)))
