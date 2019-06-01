

(load "home-page")  
;; mongolabs-build
(defn mongolabs-create [mongolabs-config my-collection pages-to-check error-length]     

  (let [ mongodb-user-pass-uri (get mongolabs-config :MONGODB_USER_PASS_URI) 
         {:keys [conn db]} (mong-core/connect-via-uri mongodb-user-pass-uri) ]

					  (defn delete-mongolabs []
					    (mong-coll/remove db my-collection))

	  (defn put-item-mongolabs [ check-record]
					     ( let [ { check-url :check-url 
                    the-date :the-date
                     the-html :the-html
                               page-ok :page-ok
																		  time-took :time-took } check-record 
					     						 trunc-html (trunc-string the-html error-length)                        
					     						 adjusted-date (easy-date the-date)
                 total-bytes (count the-html)
					            new-record {  :_id adjusted-date
					            	   :check-url check-url
                     :total-bytes total-bytes
                     :first-html trunc-html
                       :page-ok page-ok
																		   :time-took time-took} ]		
					         (mong-coll/insert db my-collection new-record)))

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

(comment  "" ((:begins-all my-db-obj) "2019-05")         )
(defn begins-all-mongolabs "" [begins-with]
    (let [ date-plus1(date-plus-1 begins-with) ]
    (println "mon" begins-with "---" date-plus1)
       (mong-coll/find-maps db my-collection {:_id { $gte begins-with $lt date-plus1 }}) ))

(comment  "year of www.sffaudio" ((:date-begins-amazonica my-db-obj) "2019" "www.sffaudio.com")         )
(defn begins-one-mongolabs "" [begins-with page-to-check]
    (let [  date-plus1   (date-plus-1 begins-with) ]
	         (mong-coll/find-maps db my-collection { $and [{:_id { $gte begins-with $lt date-plus1 }}
                                                         {:check-url page-to-check}] })))
  {:delete-table delete-mongolabs
			:begins-all begins-all-mongolabs 
   :begins-one begins-one-mongolabs 
   :put-item put-item-mongolabs}
)

)


