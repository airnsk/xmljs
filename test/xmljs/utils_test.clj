(ns xmljs.utils-test
 (:use clj-http.fake)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [xmljs.utils :refer :all]
            [clojure.data.json :as json]))


(deftest test-generate_sendxml
(testing "send xml waybill"
  (let [ response (with-fake-routes {

      "http://localhost:3000/test" (fn [request]

            (is (= (:server-port request) 8080))
            (is (= (:multipart request) "dshjdhsj"))


             {:status 200 :headers {} :body "kljsl" }  )
            }


    (sendxml (slurp (clojure.java.io/resource "tests_data/testWaybill.json")) ) )  ]

    (is (= (:status response) 200)))))



    (comment
        (run-tests)
        (pr-str {:a 1 :b 2})
      )
