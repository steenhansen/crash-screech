### Crash-sms ﻿﻿﻿


<a name='fast-start'></a>

Crash-sms is a Clojure program that runs on Heroku at https://fathomless-woodland-85635.herokuapp.com/ which checks a list of websites every 2 hours and texts a list of phones when a website does not conform to a valid state. Non cached website pages are checked for database, WordPress, and Node.js errors and or crashes. The Heroku add-on Till Mobile handles the SMS messaging, while Temporize Scheduler add-on handles the cron jobs.


System Setup:

 - get Java Windows x64  [jdk-8u271-windows-x64.exe](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

 - get Leiningen [installer](https://djpowell.github.io/leiningen-win-installer/)

 - $ cd crash-sms

 - To download Clojure dependencies:

       $ lein deps            

### Compile crash-sms in Emacs with Cider
```
M-x load-file /crash-sms/src/core.clj
M-x cider-jack-in
M-x cider-ns-refresh
M-x cider-repl-set-ns
```  

##### Serve crash-sms with memory database
```
core> (-local-main USE_FAKE_DB LOCAL_CONFIG)
```

##### Serve crash-sms with MongoDB
```
core> (-local-main USE_MONGER_DB LOCAL_CONFIG)
```

##### Serve crash-sms with DynamoDB
```
core> (-local-main USE_AMAZONICA_DB LOCAL_CONFIG)
```

##### Serve crash-sms with Heroku MongoDB
```
core> (-local-main USE_MONGER_DB HEROKU_CONFIG)
```

##### Run tests with mock database
```
core-test> (mock-tests)
```

##### Run tests with both a MongoDb and a DynamoDB database
```
core-test> (all-tests)
```


###
##### Last two months data
```
http://localhost:8080/
```

##### Cron job to check web pages
```
http://localhost:8080/cron-execution-test
```

##### SMS test
```
http://localhost:8080/sms-send-test
```

###
##### Heroku Config Vars
![visual explanation](https://github.com/steenhansen/crash-sms/blob/master/git-images/heroku-config-vars.png)

##### Temporize Cron Config Vars
	:CRON_URL_DIR
![visual explanation](https://github.com/steenhansen/crash-sms/blob/master/git-images/temporize-cron-vars.png)

##### Till SMS Config Vars
	:PHONE_NUMBERS 
	:SEND_TEST_SMS_URL
	:TILL_API
	:TILL_USERNAME
![visual explanation](https://github.com/steenhansen/crash-sms/blob/master/git-images/till-sms-vars.png)







