# Example Spring-Boot Project

## Description

Spring boot project with

* **API** (Service, DTO, Exception, Validation)
* **Persistence** (Spring Data JPA & Repository, Liquibase, H2 Database)
* **REST** controller (CRUD)
* **TLS enabled** and properly configured (optionally redirecting HTTP to HTTPS when enabling the HTTP port).
* **Access Log filter**, logging all requests (method, URI, response status and execution time)
* **Performance Logging Aspect**, logging performance tree of nested service calls

### Setting up TLS

The application uses a **TLS server certificate** issued by a **test CA** (certificate authority). Clients need to trust
this CA in order to connect. The test client already has a properly set up client truststore including this CA
certificate.

When connecting with other clients (browser, Postman, ...), you need to add this CA as **trusted root CA** (browser:
certificates, add trusted root certificate). The CA certificate is located in the project root (`test_ca_001.cer`).

## Build

To build this project with Gradle (default tasks: _clean build install_):

    ./gradlew

## Run

To start the server:

    ./gradlew start

The `:bootrun` task will remain in the `EXECUTING` state to keep the server alive, until it is terminated with _CTRL-C_.

## API Documentation (Swagger)

API documentation reachable at [https://localhost:8443/swagger-ui/](https://localhost:8443/swagger-ui/)

## Actuator Endpoints

* Info: [https://localhost:8443/info](https://localhost:8443/info)
* Health: [https://localhost:8443/info](https://localhost:8443/health)
* Metrics: [https://localhost:8443/info](https://localhost:8443/metrics)
