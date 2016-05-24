
(ns xmljs.templates
  (:gen-class)
  (:use [clojure.data.xml]
        [xmljs.templateWaybill]
        [xmljs.templateTakeOver]
        [xmljs.templateInvoice])
)



(defn createtags [hashmapa params]

  (let [typedoc (:typedoc params)]
    (case typedoc
      "waybill" (createWaybill hashmapa)
      "invoice" (createInvoice hashmapa)
      "takeover" (createTakeOver hashmapa)
      "error" )))
