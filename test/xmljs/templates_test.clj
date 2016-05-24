(ns xmljs.templates-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [xmljs.templates :refer :all]
            [xmljs.handler :refer :all]
            [clojure.data.json :as json]))

(deftest test-generate_template
  (testing "post route waybill"
    (let [hm (slurp (clojure.java.io/resource "tests_data/testWaybill.json"))
          response (app (mock/request :post "/fillers/waybill/111" hm ))]

      (is (= (:status response) 200))
      (is (= (slurp (clojure.java.io/resource "tests_data/foo.xml") :encoding "windows-1251"))
              (slurp (clojure.java.io/resource "tests_data/testWaybill.xml") :encoding "windows-1251")) )) )

(comment
    (run-tests)
    )
