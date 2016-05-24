(ns xmljs.templateXML
  (:gen-class)
  (:use [clojure.data.xml]
        [xmljs.utils])
)



(defn adat [v]
  " drop attr if empty"
    (apply merge (for [k v
          :when (not (empty? (str (val k))))]
           (apply hash-map k ))))

(adat {:A 1  :b " "})


(defn contactsXmlTemplate [jsn]
    (let [phone (:phone jsn)
          fax (:fax jsn)]
    (when (or (not (empty? phone ))
              (not (empty? fax )))
              [:Контакт (adat {:Факс fax
                               :Тлф phone})])))

(defn bankingXmlTemplate [jsn]
    (let [account (:bankAccount jsn)
          bik (:bankBik jsn)
          name (:bankName jsn)]
    (when (or (not (empty? account ))
              (not (empty? bik ))
              (not (empty? name )))
        [:БанкРекв (adat {:НомерСчета account})
        [:СвБанк (adat {:БИК bik
                        :НаимБанк name })]])))

(defn companyInfoXmlTemplate [jsn]
  [:ИдСв {}
    (if (:entrepreneur jsn)
       [:СвФЛ (adat {:ИННФЛ (:inn jsn)})
          [:ФИОИП (adat {:Фамилия (:lastName (:fio jsn))
                         :Имя (:firstName (:fio jsn))
                         :Отчество (:middleName (:fio jsn))})]]
       [:СвЮЛ (adat {:ИННЮЛ (:inn jsn)
                     :КПП (:kpp jsn)
                     :НаимОрг (:orgName jsn)})])])

(defn addressXmlTemplate [jsn]
[:Адрес {}
  (if (:rf jsn)
    [:АдрРФ (adat {:Индекс (:index jsn)
                   :КодРегион (:region jsn)
                   :Район (:district jsn)
                   :Город (:city jsn)
                   :НаселПункт (:locality jsn)
                   :Улица (:street jsn)
                   :Дом (:house jsn)
                   :Корпус (:housing jsn)
                   :Кварт (:flat jsn)})]
      [:АдрИно (adat {:КодСтр (:countryCode jsn)
                      :АдрТекст (:text jsn)})])])



(defn dutyPersonXmlTemplate [jsn]
[:ФИО (adat {:Фамилия (:lastName jsn)
            :Имя (:firstName jsn)
            :Отчество (:middleName jsn)
            :Должность (:rank jsn)})])


(defn fioXmlTemplate [jsn]
[:ФИО (adat {:Фамилия (:lastName jsn)
            :Имя (:firstName jsn)
            :Отчество (:middleName jsn)
            })])



(defn operatorXmlTemplate [jsn]
  [:СвУчДокОбор {:ИдОтпр (str (:id (:operator jsn)) (:senderId jsn))
                 :ИдПок (str (:id (:operator jsn)) (:recipientId jsn))}
    [:СвОЭДОтпрСФ {:ИННЮЛ (:inn (:operator jsn))
                   :ИдЭДОСФ (:id (:operator jsn))
                   :НаимОрг (:name (:operator jsn))}]])



(defn signerXmlTemplate [jsn]
  (if (:entrepreneur jsn)
  [:ИП (adat {:ИННФЛ (:inn jsn)
             :СвГосРегИП (:regNumber jsn)})
             [:ФИО (adat {:Фамилия (:lastName jsn)
                         :Имя (:firstName jsn)
                         :Отчество (:middleName jsn)})]]))

(defn commonXmlTemplate [jsn]
        {:ИдФайл (:fileId jsn)
         :ВерсПрог (:programVersion jsn)
         :ВерсФорм (:formatVersion jsn)}
    (when (not (empty? (:operator jsn)))
     (operatorXmlTemplate jsn))
)

(defn attorneyXmlTemplate [jsn]
    [:ДоверенИсполн (adat {:НомДоверен (:attorneyNumber jsn)
                           :ДатаДоверен (:attorneyDate jsn) })
            [:ВыданаКем (adat {:НаимОргКем (:orgName (:givenBy jsn))
                               :ДолжнКем (:rank (:givenBy jsn))
                               :ДопСведКем (:addInfo (:givenBy jsn))})
              (fioXmlTemplate (:fio (:givenBy jsn)))
            ]
            [:ВыданаКому (adat {:Должн (:rank (:givenTo jsn))
                               :ДопСведКому (:addInfo (:givenTo jsn))})
              (fioXmlTemplate (:fio (:givenTo jsn)))
            ]
    ]
)

(defn unknownParticipantXmlTemplate [jsn]

[:НаимГОП {}
  (if (not (empty? (:orgName jsn)))
    [:НаимОрг {}
      (:orgName jsn)
    ]
    (when (not (empty? (:fio jsn)))
    [:ФИОИП (adat {:Фамилия (:lastName (:fio))
                   :Имя (:firstName (:fio))
                   :Отчество (:middleName (:fio))})
    ]))])
