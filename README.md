# rest-ful-service-with-javalin

This is a Javalin example project which implements RESTful API for money transfers between bank accounts.


##  Installation Prerequisites
  - Java 11 
  - maven
  
##  Usage 

### run local
  ```bash
  ./mvnw clean package && start.sh
  ```

### create bank account 
```
curl --request POST \
  --url http://localhost:8080/accounts \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"holder":"Ana","initialBalance":10}'
```
### get bank account details 
```
curl --request GET \
  --url http://localhost:8080/accounts/1337dd8e-2085-49d5-b348-33779553d302 \
  --header 'accept: application/json'
```

### transfer money
 - create second account 
```
curl --request POST \
  --url http://localhost:8080/accounts \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"holder":"Bob","initialBalance":100}'
```
 - transfer money 
```
curl --request POST \
  --url http://localhost:8080/transfers \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"payerAccountId":"1337dd8e-2085-49d5-b348-33779553d302","beneficiaryAccountId":"2d07591f-c035-4cac-8cc5-9d68ac09b8e5","amount" : 2}'
``` 

##Development

### Technologies used:

* [Java](https://www.java.com/)
* [Javalin](https://javalin.io/) as REST server
* [H2](https://www.h2database.com/html/main.html) in memory DB
* [JUnit5](https://junit.org/junit5/), and  [Unirest](http://kong.github.io/unirest-java) for testing
* [Maven](https://maven.apache.org/) as mvn
    
## limitation to keep it simple

* the account balance and transfer amount is expected and stored as integer (cold be considered as the smallest denomination of the currency)
* there is no currency yes :)
* there is in-memory database used - no persistence after shut down

  
## Contributions  

I don't believe, but hey, if anybody wants.
Please you are welcome.
 
  