# ${projectDescription}

## Description

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
