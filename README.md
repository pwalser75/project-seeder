# Project Seeder

The Project Seeder is an executable (Java, command line) which allows to **create (seed) new projects based on templates**.
Several templates are already included:
* **simple**: Creates a simple Java project (Gradle-based)
* **webapp**: Creates a Java webapp project with a Servlet and a REST/JSON web service (Gradle-based)
* **jee**: Creates a Java EE multi-module project with persistence and web services (Gradle-Based)

## Usage

The project-seeder JAR file is executable, and will lead you through the creation of a project step-by-step.
```
java -jar build\libs\project-seeder-1.0.0-SNAPSHOT.jar
```
Usage example:
```
Available templates:
- jee
- simple
- webapp
Choose template: webapp
Project group (org.test): ch.frostnova
Project name (some-project): example-webservice
Project version (1.0.0-SNAPSHOT): 2.0.0-SNAPSHOT
Base package (ch.frostnova.example.webservice):
Base output dir (D:\idx-workspace\jee-demo\project-seeder\..): d:\temp

Seeding project...
Template dir: D:\idx-workspace\jee-demo\project-seeder\templates\webapp
Output dir: d:\temp\example-webservice
basePackage: ch.frostnova.example.webservice
basePackagePath: ch/frostnova/example/webservice
projectGroup: ch.frostnova
projectName: example-webservice
projectVersion: 2.0.0-SNAPSHOT
>> d:\temp\example-webservice\build.gradle
>> d:\temp\example-webservice\src
>> ...
Project created at: d:\temp\example-webservice
```
