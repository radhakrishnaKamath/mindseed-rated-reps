(ns demo-app.effects
  (:require
   [re-frame.core :refer [dispatch]]
   [demo-app.db :as db :refer [app-db]]))

(def baseurl "https://reqres.in/api")

(def url_map
  {:sign-in "users?page=2"
   :messages "messages"
   :signout "auth/signout"})

(defn get-url [endpoint]
  (str baseurl "/" (endpoint url_map)))

(defn- handle-fetch-promise
  [promise & {:keys [on-success on-error parse-json]}]
  (-> promise
      ;; network error
      (.catch (fn [err] (on-error {:data err :type :network-error})))

      ;; fecth resolves also 400 and 500 errors
      (.then (fn [res]
               (if (not (nil? res))
                 (let [ok (.-ok res)
                       status (.-status res)
                       error-type (cond
                                    ok nil
                                    (= status 403) :unauthorized
                                    :else :http-error)
                       handle-data (fn [data]
                                     (if ok
                                       (on-success {:data data :status status :ok ok})
                                       (on-error   {:data data :status status :type error-type})))
                       has-json (if (nil? parse-json) false
                                    parse-json)]

                   (-> (if has-json
                         (-> (.json res)
                             (.then #(js->clj %1 :keywordize-keys true))
                             (.then
                              #(if (vector? %1)
                                 (vec (map clojure.walk/keywordize-keys %1))
                                 %1)))
                         (.text res))
                       (.then handle-data))))))))

#_(.then #(do (log %) %))

(defn fetch-json [& {:keys [url endpoint endpoint-path headers method body on-success on-error fetch-fn]}]
  "Helper for JSON rest api request"
  (let [react-fetch    (if (nil? fetch-fn)
                         js/fetch
                         fetch-fn)
        path           (apply str (map (partial str "/") endpoint-path))
        req-url        (if (nil? endpoint)
                         url
                         ;;(get-url endpoint)
                         (str (get-url endpoint) path))

        default-header {"Content-Type" "application/json" "Accept" "application/json"}
        optional-body  (if body
                         {"body" (js/JSON.stringify (clj->js body))}
                         {})

        args (merge {"method"   method
                     "credentials" "include"
                     "headers" (merge default-header headers)}
                    optional-body)
        query (react-fetch req-url (clj->js args))]

    (handle-fetch-promise query :on-error on-error :on-success on-success :parse-json true)))

(defn signin [& {:keys [user on-success on-error fetch-fn]}]

  "Credentials schema is
  {
      username (string): Username or email. ,
      password (string): Password
  }.
  On succesful request return object that contains user data and autehtication cookie."
  (let [fetch (if (nil? fetch-fn) fetch-json fetch-fn)]
    (fetch
     :method "GET"
     :endpoint :sign-in
     :on-success on-success
     :on-error   on-error)))
