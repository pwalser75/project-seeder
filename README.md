# Project Seeder

The Project Seeder is an executable (Java, command line) which allows to **create (seed) new projects based on templates**.

Several templates are already included:
* **simple**: Creates a simple Java project (Gradle-based)
* **webapp**: Creates a Java webapp project with a Servlet and JAX-RS web service (Gradle-based)
* **spring-boot**: Creates a single-module Spring Boot project with JPA and JAX-RS web service (Gradle-Based)
* **jee**: Creates a Java EE multi-module project with JPA and JAX-RS web service (Gradle-Based)

## Usage

The project-seeder JAR file is executable, and will lead you through the creation of a project step-by-step.
```
java -jar build\libs\project-seeder-1.0.0-SNAPSHOT.jar
```
Usage example:
```
Available templates:

- CDI-JavaSE
  Simple Java 8 project with CDI 2.0 - JavaSE

- Documentation
  AsciiDoc based documentation project with PDF/HTML5 output

- JEE
  Multi-module Java EE 7 project

- Java
  Simple Java 8 project with logging and test (JUnit/Mockito)

- Spring-Boot
  Spring Boot project with JAX-RS, JAX-B and JPA

- Webapp
  Java web project with Servlets, Freemarker templates and a REST/JSON web service

Choose template: web
Choose template (webapp):
Project group (org.test): ch.frostnova
Project name (webapp-project): example-webservice
Project description (example-webservice):
Project version (1.0.0-SNAPSHOT): 2.0.0-SNAPSHOT
Base package (ch.frostnova.example.webservice):
Base output dir (D:\idx-workspace\project-seeder\..): d:\temp

Seeding project...
Template dir: D:\idx-workspace\project-seeder\templates\webapp
Output dir: d:\temp\example-webservice
basePackage: ch.frostnova.example.webservice
basePackagePath: ch/frostnova/example/webservice
projectDescription: example-webservice
projectGroup: ch.frostnova
projectName: example-webservice
projectVersion: 2.0.0-SNAPSHOT
.........
Project created at: d:\temp\example-webservice
```

## Create your own templates

To create your own templates, create a directory within the **templates** folder. The folder name will be the name of the template. When processing the template, all files within the chosen template folder get copied to the chosen target directory, replacing following placeholders in files(with suffixes `"txt", "md", "xml", "java", "gradle", "ts", "js", "json"`) and directory names:

 - `${user}`: creation user name (system property: `user.name`)
 - `${date}`: current date (yyyy-MM-ss)
 - `${datetime}`: current date and time (yyyy-MM-ss HH:mm)
 - `${projectGroup}`: project group
 - `${projectGroup}`: project group
 - `${projectName}`: project name
 - `${projectDescription}`: project description
 - `${projectVersion}`: project version
 - `${basePackage}`: base package
 - `${basePackagePath}`: base package path (`.` replaced by `/`)

