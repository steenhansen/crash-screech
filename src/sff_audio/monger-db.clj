

(load "home-page") 
(load "check-data") 



;;; (-main "monger" "localhost")

(defn date-plus-1 [begins-with]
(let [  date-vector  (clj-str/split begins-with #"-") 
							 last-string (last date-vector)
        last-number (read-string last-string) 
							 last-plus1 (+ last-number 1)
        last-padded (left-pad last-plus1 2)
        but-last (pop date-vector)
        plus1-vector (conj   but-last last-padded)
        date-plus1   (clj-str/join "-" plus1-vector)
    ]
    date-plus1))




(defn mongolabs-build [mongolabs-config my-collection pages-to-check ]    
  (let [ mongodb-user-pass-uri (get mongolabs-config :MONGODB_URI) 
         {:keys [conn db]} (mong-core/connect-via-uri mongodb-user-pass-uri)]


										; put-items-monger
											    (defn put-items-monger [ check-records]
											     ( let [ fixed-dates ( prepare-data check-records )            ]
																      (mong-coll/insert-batch db my-collection fixed-dates)))

																  (defn delete-table-monger []
																    (mong-coll/remove db my-collection))

												
																;; put-item-monger
																(defn put-item-monger [ check-record]
																     ( let [ fixed-dates ( prepare-data [check-record] )
											                   fixed-rec (first fixed-dates)				             ]
																      (mong-coll/insert db my-collection fixed-rec)))

											;; get-all-monger
											(comment  "" ((:get-all-monger my-db-obj) "2019-05")         )
											(defn get-all-monger "" [begins-with]
											    (let [ date-plus1(date-plus-1 begins-with) ]
											    (println "mon" begins-with "---" date-plus1)
											       (mong-coll/find-maps db my-collection {:_id { $gte begins-with $lt date-plus1 }}) ))

											;; get-url-monger
											(comment  "year of www.sffaudio" ((:get-url-monger my-db-obj) "2019" "www.sffaudio.com")         )
											(defn get-url-monger "" [begins-with page-to-check]
											    (let [  date-plus1   (date-plus-1 begins-with) ]
												         (mong-coll/find-maps db my-collection { $and [{:_id { $gte begins-with $lt date-plus1 }}
											                                                         {:check-url page-to-check}] })))
)


  {:delete-table delete-table-monger
			:get-all get-all-monger 
   :get-url get-url-monger 
   :put-item put-item-monger
   :put-items put-items-monger}
)


