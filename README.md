# Building microservices

## About
This is an implementation of https://www.youtube.com/watch?v=ZyK5QrKCbwM using the bellow versions:
- org.springframework.boot 2.0.5.RELEASE
- org.springframework.cloud Finchley.SR1
- Java 11

## Architecture
![Building microservices architecture](https://github.com/danielcasique/building-microservices/blob/master/images/building_microservices.png?raw=true)

### config-server
Uses the spring-cloud-config-server in order to allow other modules to get the properties from a centralized site.
The folder microservices-config contains the properties files for the modules eureka-service, reservation-client, reservation-service, and hystrix-dashboard. This folder could be located in the github repository or in a local system. The property spring.cloud.uri must be modified according to this set up. If the folder microservices-config is located in a local system, it must be initialized as a git hub repository anyway. (Just use git init)

### eureka-service
Uses the spring-cloud-starter-netflix-eureka-server to enable a server for registering, discovering and load balancing microservices. ([more info](https://spring.io/guides/gs/service-registration-and-discovery/))
