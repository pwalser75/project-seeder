# ${projectDescription}

## Description

Spring boot project with

* API (Service, DTO, Exception)
* Persistence (Spring Data Repository, JPA)
* Service Layer
* REST controller (CRUD)
* TLS enabled and properly configured (redirect HTTP to HTTPS).
* Access Log filter, logging all requests (method, URI, response status and execution time)

### Setting up TLS

The application uses a TLS server certificate issued by a test CA (certificate authority).
Clients need to trust this CA in order to connect. The test client already has a properly set up client truststore including this CA certificate.

When connecting with other clients (browser, Postman, ...), you need to add this CA as trusted root CA (browser: certificates, add trusted root certificate).
The CA certificate is located in the project root (`test_ca_001.cer`).

## Artefact

To include this project in another:

with Gradle:

    compile '${projectGroup}:${projectName}:${projectVersion}'

with Maven:

    <dependency>
       <groupId>${projectGroup}</groupId>
       <artifactId>${projectName}</artifactId>
       <version>${projectVersion}</version>
    </dependency>

## Build

To build this project with Gradle (default tasks: _clean build install_):

    gradle
    
## Run
    
To build this project with Gradle:
    
    gradle bootRun

## API doc (Swagger)

API documentation reachable at [https://localhost/swagger-ui.html](https://localhost/swagger-ui.html)
