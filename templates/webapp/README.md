# Web application example

## Build the application

To build the application with Gradle:

	gradle

## Run / deploy the application

### Local (project)
The following command will download (`container/download`), extract (`container/extract`) and start a local Tomcat container, and deploy the application:

    gradle run -i

Use `CTRL + C` to stop the container.

### Remote (existing local or remote Tomcat installation)
Configure the tomcat settings in the `gradle.properties` (for the Tomcat user you configured). <br/>
Example:

    tomcat.host=localhost
    tomcat.port=8080
    tomcat.user=tomcat
    tomcat.password=Secret-007

To deploy the application WAR on the specified Tomcat, use:

    gradle deploy
	
## Test the endpoints:
- http://localhost:8080/test/hello (Hello Servlet)
- http://localhost:8080/test/info (Info Servlet - prints request information)
- http://localhost:8080/test/template (Example Servlet with Freemarker)
- http://localhost:8080/test/api/weather (Web service)
