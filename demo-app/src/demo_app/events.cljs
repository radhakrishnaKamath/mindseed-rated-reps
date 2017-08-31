(ns demo-app.events
  (:require
   [re-frame.core :refer [reg-event-db after register-handler dispatch]]
   [clojure.spec.alpha :as s]
   [demo-app.db :as db :refer [app-db]]
   [demo-app.effects :as effects]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(defonce debug-level (atom :debug))

(defn debug [thing]
  "Log to console, if goog.DEBUG is true and log level is :info of :debug"
  (when (and goog.DEBUG
             (or
              (= :debug @debug-level)
              (= :info @debug-level)))
    (js/alert thing)))

(defn register-handler-for [event-name handler-fn]
  "Simplify register handler calls by automatically registring middleware
   and by droping event name from event arguments"
  (register-handler
   event-name
   (fn [db evt]
     (debug (str "Dispatch " event-name))
     (apply handler-fn (concat [db] (rest evt))))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 validate-spec
 (fn [_ _]
   app-db))

(reg-event-db
 :set-greeting
 validate-spec
 (fn [db [_ value]]
   (assoc db :greeting value)))

(reg-event-db
 :set-page
 (fn [db [_ page]]
   (assoc db :page page)))

(reg-event-db
 :auth-success
 (fn [db [_ result]]
   (assoc db :auth-success result)))

(register-handler-for
 :login
 (fn [db usr-pwd]
   (let [sign-in effects/signin ]
     (sign-in :user {:email (:user usr-pwd) :password (:pwd usr-pwd)}
              :on-success (fn [user-res] (js/alert user-res))
              :on-error
              #(js/alert %)))))
