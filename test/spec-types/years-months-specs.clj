
(alias 's 'clojure.spec.alpha)

(def yyyy-mm-regex #"^([\d]{4})-(0[1-9]|1[0-2])$")
(def yyyy-mm-extra #"^([\d]{4})-(0[1-9]|1[0-2])(-.*)*$")

(def yyyy-mm-dd-regex #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")
(def yyyy-mm-dd-extra #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])(-.*)*$")

(s/fdef crash-screech.years-months/adjusted-date
  :args (s/cat :date-str string?))

(s/fdef crash-screech.years-months/current-month
        :args (s/alt :nullary (s/cat)
                     :unary (s/and string? #(re-matches yyyy-mm-extra %)
)))


(s/fdef crash-screech.years-months/current-yyyy-mm-dd
        :args (s/alt :nullary (s/cat)
                     :unary (s/and string? #(re-matches yyyy-mm-dd-extra %)
)))

(s/fdef crash-screech.years-months/current-yyyy-mm
        :args (s/alt :nullary (s/cat)
                     :unary (s/and string? #(re-matches yyyy-mm-extra %)
)))

(s/fdef crash-screech.years-months/date-to-yyyy-mm
  :args (s/cat :ymd-date java-time/local-date?))

(s/fdef crash-screech.years-months/date-to-yyyy-mm-dd
  :args (s/cat :ymd-date java-time/local-date?))

(s/fdef crash-screech.years-months/instant-time-fn
  :args (s/cat))

(s/fdef crash-screech.years-months/month-name
        :args (s/alt :unary (s/and integer? #(s/int-in-range? -1 12 % ))
                     :binary (s/cat :month-offset (s/and integer? #(s/int-in-range? -1 12 % ) )
                                    :yyyy-mm (s/and string? #(re-matches yyyy-mm-extra %))
)))

(s/fdef crash-screech.years-months/prev-month
        :args (s/alt :nullary (s/cat)
                     :unary (s/and string? #(re-matches yyyy-mm-extra %)
)))



(s/fdef crash-screech.years-months/prev-yyyy-mm
        :args (s/alt :nullary (s/cat)
                     :unary (s/and string? #(re-matches yyyy-mm-regex %)
)))

 (s/fdef crash-screech.years-months/trunc-yyyy-mm-dd
   :args (s/cat :ymd-extra (s/and string? #(re-matches yyyy-mm-dd-extra %) )))

 (s/fdef crash-screech.years-months/trunc-yyyy-mm
   :args (s/cat :ym-extra  (s/and string? #(re-matches yyyy-mm-extra %) )))

(s/fdef crash-screech.years-months/yyyy-mm-to-ints
 :args (s/cat :yyyy-mm   (s/and string? #(re-matches yyyy-mm-regex %) )))

















(in-ns 'spec-calls)
