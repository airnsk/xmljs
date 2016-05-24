(ns xmljs.handler
  (:gen-class)
  (:use [clojure.data.xml]
        [xmljs.utils])
  (:require [compojure.core :refer :all]
      [compojure.handler :as handler]
      [compojure.route :as route]
      [org.httpkit.server :as ohs]
      [xmljs.templates :as templates]
      [clojure.data.json :as json] ))



(comment
      (-main)
      (stop-server)
      (print :resource-paths)

      )


(defn handler [req]

      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    "hello HTTP!"})



(defn htest [req]
(prn (str "req from clj-http" (:headers req) (slurp (:body req))))
     {:status  200
      :headers {"Content-Type" "text/html"}
      :body    "req"})


(defn getJSONfromXML [req]

  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (:params req) })


(defn postXMLfromJSON [req]

  (let [rjson (json/read-str  (slurp (:body req))  :key-fn keyword)
        tags  (templates/createtags rjson (:params req))]
        (savexml tags)
        ;;(sendxml tags)

  {:status  200
   :headers {"Content-Type" "application/xml" }
   :body  (memit-str tags :encoding "windows-1251")}))


(defroutes app
     (GET "/" [] handler)
     (ANY "/test/:id" [] #'htest)
     (GET "/fillers/:typedoc/:id" [] #'getJSONfromXML)
     (POST "/fillers/:typedoc/:id" [] #'postXMLfromJSON)
     (route/not-found "<p>Page not found.</p>"))


(defonce server (atom nil))
(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))
(defn -main []
  (reset! server (ohs/run-server #'app {:port 8080})))
