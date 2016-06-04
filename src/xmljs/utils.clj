(ns xmljs.utils
  (:gen-class)
  (:use [clojure.data.xml]
        [digest]

        ;;[clojure.contrib.str-utils]
        )
  (:require
      [clojure.zip :as czip]
      [clojure.xml :as cxml]
      [clojure.data.zip.xml :refer [tag= attr text xml-> xml1->]]
      [clojure.java.io :as io]
      [clojure.data.json :as json]
      [clj-http.client :as client]
      [org.httpkit.client :as http]
      [crypto.random :as crypto.random]

     ))


(defmacro not-empty? [checkvar]
       `(not (empty? ~checkvar)))


(defn memit-str
  "Emits the Element to String and returns it"
  [e t c]
  (let [^java.io.StringWriter sw (java.io.StringWriter.)]
    (emit e sw t c)
    (.toString sw)))

(defn savexml [tags]
  (with-open [out-file (io/writer "foo.xml" :encoding "windows-1251") ]
      (emit tags out-file :encoding "windows-1251"))
)




(defn sendxml [tags]

    (let [baos (java.io.ByteArrayOutputStream.)
          obaos (io/writer baos :encoding "windows-1251")]

      (emit tags obaos :encoding "windows-1251")
      (.close obaos)



      (let [cr (crypto.random/url-part 10)
            ba (.toByteArray baos )
            jmeta {"fileName" (str "test" (crypto.random/url-part 10) ".txt")

                    "sha256hash" (digest/sha-256 ba)
                    "size" (- (alength ba) 2 )
                    "mediaType" "text/xml"

                    }]





     (client/post (str "http://localhost:8080/test/" cr)


       {

      :headers {"cookie" "sessionid=; rememberme="
                   "Accept" "*/*"
                   }

      :multipart [
                     {:name "name" :content (crypto.random/url-part 10) }

                     {:name "chunk" :content "0"}
                     {:name "chunks" :content "1"}

                    {:name "meta"  :content (json/write-str jmeta)  }

                    {:name "file" :filename (str "test" (crypto.random/url-part 10) ".txt") :content  (java.io.ByteArrayInputStream. ba) :Content-Type "text/plain"}
                    ]})

                                                     )))

; (def nacxml
;          (-> "t1251.xml"
;               clojure.java.io/resource
;               clojure.java.io/input-stream
;               cxml/parse))


; (def zipped (czip/xml-zip nacxml))
; (xml-> zipped :Файл :СвУчДокОбор (attr :ИдОтпр))
; (xml1-> zipped :Файл :Документ (attr :КНД))
; (xml1-> zipped :Файл :Документ :СвТНО :ГрузОт :ГрузОтпр :ИдСв :ИдСв :СвЮЛ (attr :ИННЮЛ) )

;;(xml-> zipped :track (attr :id))
;;(xml-> zipped  :Файл :Документ :СвТНО :ТН :Таблица :СвТов (attr :Брутто))
;;(print zipped)
;;(xml1-> zipped :Файл :Документ :СвТНО :ГрузОт :ГрузОтпр :ИдСв :ИдСв)
;;(for [x (xml-seq nacxml) :when (= :СвТов (:tag x))] (:attrs x))


(defn rename-keys
  "Returns the map with the keys in kmap renamed to the vals in kmap"
  {:added "1.0"}
  [map kmap]
    (reduce
     (fn [m [old new]]
       (if (contains? map old)
         (assoc m new (get map old))
         m))
     (apply dissoc map (keys kmap)) kmap))
