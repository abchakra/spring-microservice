# spring-microservice
Monolith to Microservice using SpringBoot




## Docker compose

- docker-compose build

- docker-compose up -d

- docker-compose down

### Asking the Eureka server for registered instances
curl -H "accept:application/json" https://u:p@localhost:8443/eureka/api/apps -ks | jq -r .applications.application[].instance[].instanceId


### URLS

EUREKA  https://localhost:8443/eureka/web

Create token https://localhost:8443/oauth/token



