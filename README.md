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
Uses the spring-cloud-starter-netflix-eureka-server to enable a server for registering, discovering and load balancing microservices. ([more info](https://spring.io/guides/gs/service-registration-and-discovery/))

### reservation-service
Spring boot application that exposes services for saving a reservation, list reservations and get a message. The service connects to config-server to get all properties. It only defines the properties to connect to config-server. Also, in the config-server is defined the properties to connect to Kafka and exposes the actuator endpoints.
The reservation-service is listening to receive any data coming from Kafka service and save a reservation.

### reservation-client
It uses spring-cloud-starter-netflix-zuul to create a proxy server. he service connects to config-server to get all properties. It only defines the properties to connect to config-server. Also, in the config-server is defined the properties to connect to Kafka, exposes the actuator endpoints, and define the routes for Zuul service. 
The reservation-client exposes the POST service reservations, which recive the data and send it to a Kafka service. 
