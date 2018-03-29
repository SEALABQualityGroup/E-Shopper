# E-shopper
This is a demo application, which demonstrates Microservice Architecture Pattern using Spring Boot and Spring Cloud.

## Functional services
It was decomposed into eight core microservices. All of them are independently deployable applications, organized around certain business capability.

 #### Accounts service
It contains general user input logic and validation. 

Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
POST	| /signup	| Crete a new account	|  | x	
PUT	| /updateAccount | Modify an account	| × | 
GET	| /existinMail/{username}	| Verify that the email is valid	|  x | 	×
GET	| /findByUsername/{username}	| Find a user	| × | 
GET	| /findAddressByIdAddress/{idAddress}	| Find an address	|  x | 
POST	| /signin	| Login an account	|   | ×

#### Login service
It allows you to save login data

Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
POST	| /save	| update login info	|x  | x	

 #### Categories service
It contains info about the category of the items you can buy
It allows you to save login data

Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
GET	| /category	| Retrivies th list of the categories	|x  | x	

 #### Products service
It contains info about the products of the items you can buy

Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
GET	| /findProduct	| Retrivies the list of the products	|x  | x	
GET	| /findProductsRandom	| Retrivies 5 random products 	|x  | x	
GET	| /findProduct/{categoryId}	| Retrivies the list of the products in the specified category	|x  | x	

#### Items service
It contains details info of the items and it manages them. 

Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
GET	| /findItems/{idProduct}	| Retrivies the list of the items in the specified product	|x  | x	
GET	| /findItemsByIdCategory/{idCategory}	| Retrivies the list of the items in the specified category 	|x  | x	
GET	| /findFeaturesItemRandom	| Retrivies 3 random items with totQuantity < 3 	|x  | x	
GET	| /findItemByCategoryFilter/{idCategory}/{priceMin}/{priceMax}	| Retrivies the filtered list of the items	in the specified category|x  | x
GET	| /findItemByProductFilter/{idProducr}/{priceMin}/{priceMax}	| Retrivies the filtered list of the items	in the specified product|x  | x
GET	| /findItemRandom	| Retrivies 3 random items|x  | x
GET	| /findItemRandomByIdProduct	| Retrivies 3 random items in the specified product|x  | x
GET	| /findItem/{idItem}	| Retrivies the item |x  | x
PUT	| /updateItem	| update an item |x  | 

 #### Cart service
 It manages the cart shop.
 
 Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
GET	| /findCartByUsernameByIdItem/{username}/{idItem}	| Retrivies the specific element of the shopping cart associated with username and item.	|x  | 
GET	| /findCartById/{idCart}	| Retrivies the specific element of the shopping cart	|  x| 
GET	| /findCart/{username}	|Retrivies the list of element of the shopping cart associated to the user 	|  x| 
GET	| /countCartItem/{username}	|Retrivies the number of element of the shopping cart associated to the user 	|  x| 
POST	| /saveCartItem	| It adds element to shopping cart	|  x| 
PUT	| /updateCartItem	| Update element to shopping cart	|  x| 
PUT	| /updateListCartItem	| Update elements to shopping cart	|  x| 
DELETE	| /deleteCartItemByIdCartItem/{idCartItem}	| Delete element to shopping cart	|  x| 
DELETE	| /deleteCartItemByUsername/{idAccount}	| Delete elements to shopping cart	|  x| 

 #### Order service
 It contains details info of the orders and it manages them.
 
  Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
POST	|/saveOrder	| Create a new order	|x  | 
POST	| /saveHistoryOrder	| Save the history of the order	|  x| 
POST	| /saveRelCartOrder	| Save the relation from the order and the cart	|  x| 
PUT	| /updateOrder	| Update the order	|  x| 
GET	| /orders/{username}	| Return the list of the orders associated to the user	|  x| 
GET	| /order/{idOrder}	| Return a single order	|  x|
GET	| /listStatusOrder	| Return the list of the possible status of the order	|  x| 
POST	| /findListOrder/{username}/{statusOrder}	| Return the list of the order with a specific status for the user	|  x| 
DELETE	| /deleteCartItemByUsername/{idAccount}	| Delete elements to the shopping cart	|  x| 

 #### Wishlist service
 
 It contains details info of the wishlist and it manages them.
 
  Method	| Path	| Description	| User authenticated	| Public
------------- | ------------------------- | ------------- |:-------------:|:----------------:|
POST	|/saveWishList	| Add a new item to the wishlist	|x  | 
GET	|/findWishList/{username}	| Return the wishlist associated with the user	|x  |
GET	|/findWishListByUsernameByIdItem/{username}/{idItem}	| Return an element of the wishlist	|x  | 
DELETE	|/deleteWishListById/{idWishList}	| Delete an element of the wishlist	|x  | 

#### Notes
- Each microservice has it's own database, so there is no way to bypass API and access persistance data directly.
- Service-to-service communication is quite simplified: microservices talking using  synchronous REST API.

## Infrastructure services

 [Spring cloud](http://projects.spring.io/spring-cloud/) provides powerful tools that enhance Spring Boot applications behaviour to implement patterns which could help us to make described core services work. The figure below shows how the E-shopper application was designed and implemented infrastructurally.

 <img alt="Infrastructure services" src="https://github.com/SEALABQualityGroup/microservices-application/blob/master/progetto-microservizi.png">

 ### Microservice-demo-config
[Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) is horizontally scalable centralized configuration service for distributed systems. In this project, it used as 'native profile'. We can consider this as an external file container. The folder *shared* contains all the .yml files of the all microservices, they only have bootstrap.yml inside them and no other file .yml. The `bootstrap.yml` contains the name of the microservice and the url of the config. For example
```yml
spring:
  application:
    name: account-server
  cloud:
    config:
      uri: http://config:8888
      fail-fast: true
```
For client-side use, there must be dependency with `spring-cloud-starter-config` within the pom.xml.

 ### Microservice-demo-registration
 The discovery of the addresses of the micro-services is delegated to a new microservice that is responsible for tracking the positions of all other ones and nothing else.
Spring Boot provides **Netflix's Eureka** or the implementation of a discovery server that allows you to record our micro services.
With Spring Boot, you can easily build Eureka Registry with `spring-cloud-starter-eureka-server dependency`, `@EnableEurekaServer` annotation and simple configuration properties.

Client support enabled with `@EnableDiscoveryClient` annotation

 ### Microservice-demo-security
 This is the only module that is not a microservice, but it is a common library used by all microservices; it invokes the `microservice-demo accounts` in order to manage the authenticatcation. This is a common layer that provides a *Credential Provider* implemented as a Spring compononent, that can be injected and used as a custom class in the security configuration of a microservices

  <img alt="security" src="https://github.com/SEALABQualityGroup/microservices-application/blob/master/progetto-microservizi 2.png">

 
The security used is `spring-boot-starter-security`.

 ### Microservice-demo-gateway
 In theory, a client could make requests to each of the microservices directly. But obviously, there are challenges and limitations with this option, like necessity to know all endpoints addresses, perform http request for each peace of information separately, merge the result on a client side. Another problem is non web-friendly protocols, which might be used on the backend.

Usually a much better approach is to use **API Gateway**. It is a single entry point into the system, used to handle requests by routing them to the appropriate backend service or by invoking multiple backend services and aggregating the results.
Netflix opensourced such an edge service, and now with Spring Cloud we can enable it with one `@EnableZuulProxy` annotation.
In this project, It used Zuul to route requests to appropriate microservices. Here's a simple prefix-based routing configuration for accounts service:
```yml
  routes:
    accounts-server:
        path: /accounts/**
        serviceId: accounts-server
        stripPrefix: false
        sensitiveHeaders:
```

This means that all requests that start with /accounts will be routed to the micro-account accounts.

## Development environment
Requirements:
-	jdk 1.8
-	Apache Maven 3.5.0+
-	MySQL 5.6
-	IDE STS (Spring Tool Suite), IntelliJ IDEA.

The startup order is important to avoid failures, it is necessary to run the microservices in this order:
1) Microservice-demo-config (listening on port 8888)
2) Microservice-demo-registration (listening on port 1111)
3) Microservice-demo-gateway (listening on port 4000)
4) eight core microservices
5) Microservice-demo-web (listening on port 3333)

For each eight core microservices there is a file .yml on the folder *shared*  of the microservice-demo-config, this file contains the **environment variable** to set in order to configure the listening port of the microservice
Example for the micorservice-demo-account service:
```yml
server:
 	context-path: /accounts
    	port: ${ACCOUNT_SERVICE_PORT}  # HTTP (Tomcat) port
```
Once the configuration has been completer, it is possible to run the microservices in two ways:
### Database
An advantage of the microservices architecture is that each core microservices  has its own database and its own schema.
The script to create the schema is, for example:
```sql
CREATE SCHEMA `microservices` DEFAULT CHARACTER SET utf8 ;
CREATE USER 'microservices'@'%' IDENTIFIED BY 'microservices';
GRANT ALL PRIVILEGES ON microservices.* TO 'microservices'@'%';
```
The schema must be configured in the *db-config.properties*:
```yml
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/microservices
spring.datasource.username=microuser
spring.datasource.password=micropassword
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
In this project, for an easly usage during developments, it was used just one schema (the script is located at the following path: *...\microservices-demo*). 
For this reason each microservice has the same *db-config.properties* properties file.
If you decide to use different schema for microservices, you must run each script properly configured (the schema name, the username and the password of the user).

There are two different ways to create the tables for each microservices:
- running the script **createXXX.sql** from the db client tool (the file  is located in each microservice at the followig path: *…\microservices-application-master\nameMicroserivices\src\main\resources.*  These files contain already the scripts in order to popolate the Typological tables)
- using the Hibernate forward engineering. Once the microservices are up and running, you must run the scripts **insertXXX.sql** from the db client tool (path:*…\microservices-application-master\noameMicroserivicessrc\main\resources.* )

### STS
STS provides a *Boot Dashboard* where you can manage the status and configure the environment variable for each microservices ; the environment variable can be set here in *Boot Dashboard --> Debug configuration*. 
It ensure that a mvn clean install command has been executed before running them by the IDE.
### command line
- go to the parent project and execute `mvn clean install`
- you can run the microservice in both mode (from microservice project base directory): 
    - using java command : *java -jar target/microservice-xxxx-0.0.1-SNAPSHOT.jar*
    - using spring boot maven plugin: *mvn spring-boot:run*
    
In the case of the eight core microservices, you must add the command to set the environment variable:
- using java command :	*java -DnameEnvironmentVariable=#port -jar target/microservice-xxxx-0.0.1-SNAPSHOT.jar*. Example:
```yml
java -DACCOUNT_SERVICE_PORT=6667 -jar microservice-demo-accounts-0.0.1-SNAPSHOT.jar
```
- using spring boot maven plugin: *mvn spring-boot:run -Drun.jvmArguments='- DnameEnvironmentVariable = #port’*. Example:
```yml
mvn spring-boot:run -Drun.jvmArguments='-DACCOUNT_SERVICE_PORT=6667'
```
To start multiple instances of a microservice you can:
 - **command line:**  each microservice has to be configured with different port number.
 - **IDE:**  it must duplicate the *Spring Boot App* and define the environment variable with different port number.


The discovery server Eureka listening on  **http://localhost:1111** (user/password)
The demo application listening on  **http://localhost:3333**

