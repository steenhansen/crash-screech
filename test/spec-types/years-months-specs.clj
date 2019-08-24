
(alias 's 'clojure.spec.alpha)
(alias 'y-m 'crash-screech.years-months)

(def yyyy-mm-regex #"^([\d]{4})-(0[1-9]|1[0-2])$")
(def yyyy-mm-extra #"^([\d]{4})-(0[1-9]|1[0-2])(-.*)*$")

;(def yyyy-mm-dd-regex #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")
(def yyyy-mm-dd-extra #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])(-.*)*$")

(s/fdef y-m/adjusted-date
  :args (s/cat :date-str string?))

(s/fdef y-m/current-month
  :args (s/alt :nullary (s/cat)
               :unary (s/and string? #(re-matches yyyy-mm-extra %))))

(s/fdef y-m/current-yyyy-mm-dd
  :args (s/alt :nullary (s/cat)
               :unary (s/and string? #(re-matches yyyy-mm-dd-extra %))))

(s/fdef y-m/current-yyyy-mm
  :args (s/alt :nullary (s/cat)
               :unary (s/and string? #(re-matches yyyy-mm-extra %))))

(s/fdef y-m/date-to-yyyy-mm
  :args (s/cat :ymd-date java-time/local-date?))

(s/fdef y-m/date-to-yyyy-mm-dd
  :args (s/cat :ymd-date java-time/local-date?))

(s/fdef y-m/instant-time-fn
  :args (s/cat))

(s/fdef y-m/month-name
  :args (s/alt :unary (s/and integer? #(s/int-in-range? -1 12 %))
               :binary (s/cat :month-offset (s/and integer? #(s/int-in-range? -1 12 %))
                              :yyyy-mm (s/and string? #(re-matches yyyy-mm-extra %)))))

(s/fdef y-m/prev-month
  :args (s/alt :nullary (s/cat)
               :unary (s/and string? #(re-matches yyyy-mm-extra %))))

(s/fdef y-m/prev-yyyy-mm
  :args (s/alt :nullary (s/cat)
               :unary (s/and string? #(re-matches yyyy-mm-regex %))))

(s/fdef y-m/trunc-yyyy-mm-dd
  :args (s/cat :ymd-extra (s/and string? #(re-matches yyyy-mm-dd-extra %))))

(s/fdef y-m/trunc-yyyy-mm
  :args (s/cat :ym-extra  (s/and string? #(re-matches yyyy-mm-extra %))))

(s/fdef y-m/yyyy-mm-to-ints
  :args (s/cat :yyyy-mm   (s/and string? #(re-matches yyyy-mm-regex %))))

















