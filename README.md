



(s/fdef f-d/fake-build
  :args (s/alt :nullary (s/cat)
               :ternary (s/cat ::mongolabs-config :mongo-config?/test-specs
               ::table-name string?
               ::pages-to-check vector?)  ;; for tests to match mongo's signature
 ))
