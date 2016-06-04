(ns xmljs.templateTakeOver
  (:gen-class)
  (:use [clojure.data.xml]
        [xmljs.utils]
        [xmljs.templateXML]))


(defn createTakeOver [hm]

  (sexp-as-element
    [:Файл (commonXmlTemplate hm)

      [:Документ (adat { :ВремДок (:documentTime hm)
                         :ДатаДок (:documentDate hm)
                         :КНД "1175006"})

        [:СвАктИ {:ДатаАкт (:takeOverDate (:takeOverInfo hm))
                  :НомАкт (:takeOverNumber (:takeOverInfo hm))
                  :НаимПервДок (:takeOverName (:takeOverInfo hm))}

                  [:Заголовок
                    (when (not-empty? (:header hm))
                      (:header hm)
                    )
                  ]
                  [:Исполнитель (adat {:ОКПО (:okpo (:senderInfo hm))})
                    (companyInfoXmlTemplate (:senderInfo hm))
                    (addressXmlTemplate (:address (:senderInfo hm)))
                    (contactsXmlTemplate (:senderInfo hm))
                    (bankingXmlTemplate (:senderInfo hm))
                  ]
                  [:ОписРабот (adat {
                                :СумУчНДСИт (:sumWithTaxTotal (:total hm))
                                :НачРабот (:workStart hm)
                                :КонРабот (:workEnd hm)
                                :СумБезНДСИт (:sumWithoutTaxTotal (:total hm))
                                :СумНДСИт (:taxTotal (:total hm))})

                        (for [x (:products hm)]
                          [:Работа (adat {
                                :Номер (:number x)
                                :НаимРабот (:name x)
                                :НаимЕдИзм (:unitsOfM x)
                                :ОКЕИ (:okei x)
                                :Цена (:price x)
                                :Количество (:amount x)
                                :СумБезНДС (:sumWithoutTax x)
                                :СумНДС (:tax x)
                                :СумУчНДС (:sumWithTax x)
                                :ИнфПолСтр (:info x)})
                                (when (not-empty? (:description x))
                                  (:description x))
                            ])

                  ] ;;ОписРабот



        (when (or (not-empty? (:executor (:passedBy hm)))
                  (not-empty? (:attorney (:passedBy hm))))
          [:Сдал (adat {:ДатаИсполн (:passedByDate (:passedBy hm))})
              (when (not-empty? (:executor (:passedBy hm)))
                  [:ПодписьИсполн
                  (adat {:Должность (:rank (:executor (:passedBy hm))) })
                    (fioXmlTemplate (:fio (:executor (:passedBy hm))))
                  ])
              (when (not-empty? (:attorney (:passedBy hm)))
                (attorneyXmlTemplate (:attorney (:passedBy hm)))
              )
           ])


        ];;СвАктИ

          (signerXmlTemplate (:signer hm))
      ];;Документ
   ];;Файл
))
