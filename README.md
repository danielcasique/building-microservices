# Building microservices

## About
This is an implementation of https://www.youtube.com/watch?v=ZyK5QrKCbwM using the bellow versions:
- org.springframework.boot 2.0.5.RELEASE
- org.springframework.cloud Finchley.SR1
- Java 11

## Architecture
![Building microservices architecture](https://github.com/danielcasique/building-microservices/blob/master/images/building-microservices.png?raw=true)

### config-server
Uses the spring-cloud-config-server in order to allow other modules to get the properties from a centralized site.
The folder microservices-config contains the properties files for the modules eureka-service, reservation-client, reservation-service, and hystrix-dashboard. This folder could be located in the github repository or in a local system. The property spring.cloud.uri must be modified according to this set up. If the folder microservices-config is located in a local system, it must be initialized as a git hub repository anyway. (Just use git init)

### eureka-service
Uses the spring-cloud-starter-netflix-eureka-server to enable a server for registering, discovering and load balancing microservices. ([more info](https://spring.io/guides/gs/service-registration-and-discovery/)). <br />
The service connects to config-server to get all properties, but it needs to set up inside the module all the properties to connect to config-server. (in the application.properties file)

### reservation-service
Spring boot application that exposes services for saving a reservation, list reservations and get a message. The service connects to config-server to get all properties, but it needs to set up inside the module all the properties to connect to config-server. (in the application.properties file). Also, in the config-server is defined the properties to connect to Kafka and exposes the actuator endpoints. <br />
The reservation-service is listening to receive any data coming from Kafka service and save a reservation.

### reservation-client
It uses spring-cloud-starter-netflix-zuul to create a proxy server. The service connects to config-server to get all properties, but it needs to set up inside the module all the properties to connect to config-server. (in the application.properties file). Also, in the config-server is defined the properties to connect to Kafka, exposes the actuator endpoints, and define the routes for Zuul service. <br /> 
The reservation-client exposes the POST service reservations, which recive the data and send it to a Kafka service. <br />
[Here](https://ricardogeek.com/microservicios-en-tiempo-real-con-kafka-y-spring-cloud/) more info about how to implement a kafka client (producers and consumers). <br />
[Here](https://medium.com/@malindudilshan389/api-gateway-with-spring-cloud-netflix-zuul-f207905fbe2b) more infor about how to implement Netflix-Zuul.

### hystrix-monitor
To support the tolerance, and monitor the services status. ([more info](https://www.baeldung.com/spring-cloud-netflix-hystrix) and [here](https://programmer.group/simple-example-of-using-hystrix-in-spring-cloud-spring-cloud-learning-note-6.html)). <br />
The reservations-service and reservation-client active the hystrix by using the tags @EnableCircuitBreaker and @HystrixCommand. <br />
The service connects to config-server to get all properties, but it needs to set up inside the module all the properties to connect to config-server. (in the application.properties file)

### kafka
It's a docker image. The docker compose file is located at kafka folder ([more info](https://zipkin.io/pages/quickstart.html))

### Zipkin
It's docker image to monitor services ([more info](https://zipkin.io/pages/quickstart.html))

## How to run (Locally)
### Prerequisites
- Set up java 11
- Set up/Install docker ([more info](https://docs.docker.com/desktop/))
- Install docker-compose (not needed on Windows)
- In the config-server module, it's needed to update the property spring.cloud.config.server.git.uri in the application.properties according with the locally in the current enviorment. The folder microservices-config must be a git repository if you want to use a local folder.

### Steps to run
1. Start the kafka docker image. Go to kafka folder and execute the below command:
docker-compose up -d
2. Start the Zipkin docker image by executing the below command:
docker run -d -p 9411:9411 openzipkin/zipkin
3. Build the jar files by executing the following command on the root path of the building-microservices
mvn clean install
4. Start each services by executing the following command (located at the roort path of the building-microservices):
java -jar config-server\target\config-server-0.0.1-SNAPSHOT.jar
java -jar eureka-service\target\eureka-service-0.0.1-SNAPSHOT.jar
java -jar hystrix-dashboard\target\hystrix-dashboard-0.0.1-SNAPSHOT.jar
java -jar reservation-client\target\reservation-client-0.0.1-SNAPSHOT.jar
java -jar reservation-service\target\reservation-service-0.0.1-SNAPSHOT.jar
