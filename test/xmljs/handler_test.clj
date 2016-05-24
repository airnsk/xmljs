(ns xmljs.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [xmljs.handler :refer :all]
            [clojure.data.json :as json]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "hello HTTP!"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))


(deftest test-getJSONfromXML
  (testing "get route waybill"
    (let [response (app (mock/request :get "/fillers/waybill/111"))]

      (is (= (:status response) 200))
      (is (= (:body response) {:typedoc "waybill", :id "111"})))))


(deftest test-postJSONfromXML
  (testing "post route waybill"
    (let [response (app (mock/request :post "/fillers/waybill/111" (slurp (clojure.java.io/resource "tests_data/testWaybill.json")) ))]

      (is (= (:status response) 200))
      (is (= (:body response)  (slurp (clojure.java.io/resource "tests_data/testWaybill.xml") :encoding "windows-1251")   )))))







(comment

  (run-tests)
  (use 'clojure.repl)
  (doc app)



  (app (mock/request :get "/"))
  )
