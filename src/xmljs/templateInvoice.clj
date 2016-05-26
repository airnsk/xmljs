(ns xmljs.templateInvoice
  (:gen-class)
  (:use [clojure.data.xml]
        [xmljs.utils]
        [xmljs.templateXML]))


(defn createInvoice [hm]

  (sexp-as-element
    [:Файл (commonXmlTemplate hm)

      [:Документ (adat {:КНД "1115101"})

        [:СвСчФакт {:НомерСчФ (:number (:invoiceInfo hm))
                    :ДатаСчФ (:date (:invoiceInfo hm))
                    :КодОКВ (:currency (:invoiceInfo hm))}


                    (when (not (empty? (:change (:invoiceInfo hm))))
                      [:ИспрСчФ (adat {:НомИспрСчФ (:number (:change (:invoiceInfo hm)))
                                       :ДатаИспрСчФ (:date (:change (:invoiceInfo hm)))})]
                    )

                    [:СвПрод
                       (companyInfoXmlTemplate (:senderInfo hm))
                       (addressXmlTemplate (:address (:senderInfo hm)))
                    ]

                    (when (and (or (not (empty? (:dispatcher hm))) ;;;;!!!!!!!!!!!!!!!  проверить логику
                              (not (empty? (:sameDispatcher hm))))
                              (:sameDispatcher hm))
                        [:ГрузОт
                        (if (:sameDispatcher hm)
                          [:ОнЖе {} "он же"]
                          [:ГрузОтпр
                            (unknownParticipantXmlTemplate (:dispatcher hm))
                            (addressXmlTemplate (:address (:dispatcher hm)))
                          ]
                        )
                        ]
                    )
                    (when (not (empty? (:consignee hm)))
                      [:ГрузПолуч
                      (unknownParticipantXmlTemplate (:consignee hm))
                      (addressXmlTemplate (:address (:consignee hm)))
                      ]
                    )

                    (for [x (:paymentDocuments hm)]
                      [:СвПРД (adat {
                            :НомерПРД (:number x)
                            :ДатаПРД (:date x)})
                        ])
                    [:СвПокуп
                      (companyInfoXmlTemplate (:recipientInfo hm))
                      (addressXmlTemplate (:address (:recipientInfo hm)))
                    ]

                    (when (not (empty? (:customInfo hm)))
                      [:ИнфПол (adat {:ТекстИнф (:text (:customInfo hm)) :ИдФайлИнфПол (:id (:customInfo hm)) })

                      ]
                    )
          ] ;;СвСчФакт
          [:ТаблСчФакт
              (for [x (:products hm)]
                [:СведТов (adat {
                      :НомСтр (:number x)
                      :НаимТов (:name x)
                      :ОКЕИ_Тов (:okei x)
                      :КолТов (:amount x)
                      :ЦенаТов (:price x)
                      :СтТовБезНДС (:valueNoTax x)
                      :СтТовУчНал (:value x)
                      :ИнфПолСтр (:info x)
                      :НалСт (:taxRate x) ;;
                      })

                      (case (:excise x)
                        "без акциза" [:Акциз
                                        [:БезАкциз
                                        "без акциза"
                                         ]
                                      ]
                        ""
                        "default" [:Акциз
                                        [:СумАкциз
                                        (:excise x)
                                         ]
                                      ])
                      (case (:tax x)
                        "без НДС" [:СумНал
                                        [:БезНДС
                                        "без НДС"
                                         ]
                                      ]
                        ""
                        "default" [:СумНал
                                        [:СумНДС
                                        (:tax x)
                                         ]
                                      ])

                      (for [c (:customsDeclaration x)]
                        [:СвТД (adat {:КодПроисх (:countryCode c)
                                      :НомерТД (:number c)})
                        ]
                      )



                  ])

                  [:ВсегоОпл (adat {:СтТовБезНДСВсего (:valueNoTax (:paymentSummary hm))
                                    :СтТовУчНалВсего (:value (:paymentSummary hm))})

                                    (case (:tax (:paymentSummary hm))
                                      "без НДС" [:СумНалВсего
                                                      [:БезНДС
                                                      "без НДС"
                                                       ]
                                                    ]
                                      ""
                                      "default" [:СумНалВсего
                                                      [:СумНДС
                                                      (:tax (:paymentSummary hm))
                                                       ]
                                                    ])
                  ]


          ] ;;ТаблСчФакт



          (signerXmlTemplate (:signer hm))
      ];;Документ
   ];;Файл
))
