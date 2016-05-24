(ns xmljs.templateWaybill
  (:gen-class)
  (:use [clojure.data.xml]
        [xmljs.utils]
        [xmljs.templateXML]))


(defn createWaybill [hm]

  (sexp-as-element
    [:Файл (commonXmlTemplate hm)

      [:Документ (adat { :ВремДок (:documentTime hm)
                         :ДатаДок (:documentDate hm)
                         :КНД "1175004"})

        [:СвТНО {:НаимПервДок "Товарная накладная"
                :НомФорм "ТОРГ-12"
                :ОКУДПервДок "0330212"}

          (let [cs (:cargoSender hm)
                inn (:inn cs)]
            (when (not (empty? inn))
                  [:ГрузОт (adat {:ОКДП (:okdp cs)})
                    [:ГрузОтпр (adat {:ОКПО (:okpo cs)})
                    (companyInfoXmlTemplate cs)
                    (addressXmlTemplate (:address cs))
                    (contactsXmlTemplate cs)
                    (bankingXmlTemplate cs)
                    (when-let [dep (:departament cs)] [:СтруктПодр {} dep])]]))

          (let [cr (:cargoRecipient hm)
                inn (:inn cr)]
            (when (not (empty? inn))
                  [:ГрузПолуч (adat {:ОКПО (:okpo cr)})
                    (companyInfoXmlTemplate cr)
                    (addressXmlTemplate (:address cr))
                    (contactsXmlTemplate cr)
                    (bankingXmlTemplate cr)]))

          (let [si (:senderInfo hm)
                inn (:inn si)]
          (when (not (empty? inn))
                  [:Поставщик (adat {:ОКПО (:okpo si)})
                    (companyInfoXmlTemplate si)
                    (addressXmlTemplate (:address si))
                    (contactsXmlTemplate si)
                    (bankingXmlTemplate si)]))

          (let [ri (:senderInfo hm)
                inn (:inn ri)]
          (when (not (empty? inn))
                  [:Плательщик (adat {:ОКПО (:okpo ri)})
                    (companyInfoXmlTemplate ri)
                    (addressXmlTemplate (:address ri))
                    (contactsXmlTemplate ri)
                    (bankingXmlTemplate ri)]))

        (when (or (not (empty? (:reasonName hm)))
                  (not (empty? (:reasonNumber hm))))
          [:Основание (adat {:ДатаОсн (:reasonDate hm)
                            :НаимОсн (:reasonName hm)
                            :НомОсн (:reasonNumber hm)})])

        (when (or (not (empty? (:cargoWaybillNumber hm)))
                  (not (empty? (:cargoWaybillDate hm))))
          [:ТранНакл (adat {
            :ДатаТранНакл (:cargoWaybillDate hm)
            :НомТранНакл (:cargoWaybillNumber hm)})])

        (when (not (empty? (:operation hm)))
          [:ВидОперации {} (:operation hm)])

          [:ТН {:ДатаТН (:waybillDate (:waybillInfo hm))
                :НомТН (:waybillNumber (:waybillInfo hm))}
            [:Таблица

              (for [x (:products hm)]
                [ :СвТов (adat {
                  :НомТов (:number x)
                  :НаимТов (:name x)
                  :ХарактерТов (:property x)
                  :СортТов (:kind x)
                  :АртикулТов (:article x)
                  :КодТов (:code x)
                  :НаимЕдИзм (:unitsOfM x)
                  :ОКЕИ_Тов (:okei x)
                  :ВидУпак (:pack x)
                  :Место (:position x)
                  :КолМест (:numOfPos x)
                  :Брутто (:brutto x)
                  :Нетто (:netto x)
                  :Цена (:price x)
                  :СумБезНДС (:sumWithoutTax x)
                  :СтавкаНДС (:taxRate x)
                  :СумНДС (:tax x)
                  :СумУчНДС (:sumWithTax x)
                  :ИнфПолСтр (:info x)})])

              [:ВсегоНакл (adat {
                    :КолМестВс (:numOfPosTotal (:total hm))
                    :БруттоВс (:bruttoTotal (:total hm))
                    :НеттоВс (:nettoTotal (:total hm))
                    :СумБезНДСВс (:sumWithoutTaxTotal (:total hm))
                    :СумНДСВс (:taxTotal (:total hm))
                    :СумУчНДСВс (:sumWithTaxTotal (:total hm))})]
              ] ;;таблица

            [:ТНОбщ (adat {
              :КолНомЗап (:productsCount (:total hm))
              :КолНомЗапПр (:productsCountInWords (:total hm))
              :ВсМест (:numOfPosTotal (:total hm))
              :ВсМестПр (:numOfPosTotalInWords (:total hm))
              :Нетто (:nettoTotal (:total hm))
              :Брутто (:bruttoTotal (:total hm))})]

          ] ;; TH

          [:ОтпускГруз (adat {
            :КодПрил (:attachments (:shipping hm))
            :КодПрилПр (:attachmentsInWords (:shipping hm))
            :СумОтпуск (:sumWithTaxTotal (:total hm))
            :СумОтпускПр (:sumInWords (:shipping hm))
            :ДатаОтпуск (:waybillDate (:waybillInfo hm))})

            (when (not (empty? (:grantedBy (:shipping hm))))
                  (dutyPersonXmlTemplate (:ОтпускРазреш (:grantedBy (:shipping hm)))))
            (when (not (empty? (:booker (:shipping hm))))
                  (dutyPersonXmlTemplate (:Бухгалтер (:booker (:shipping hm)))))
            (when (not (empty? (:sentBy (:shipping hm))))
                  (dutyPersonXmlTemplate (:ОтпускПроизв (:sentBy (:shipping hm)))))]

          [:ИнфПол {:ТекстИнф "&lt;Данные&gt;&lt;Реквизит Значение=&quot;6339dfb4-cd2f-4d0b-bbbd-4f8e735896b6&quot; Имя=&quot;ИДДокумента&quot;/&gt;&lt;/Данные&gt;"}]

        ];;СвТНО

        [:Подписант {}
          (signerXmlTemplate (:signer hm))]
      ];;Документ
   ];;Файл
))
