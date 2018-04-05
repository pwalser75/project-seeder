# Web application example

## Tomcat Setup (Windows):
1. Download Apache Tomcat 9 (64-bit Windows zip) from https://tomcat.apache.org/download-90.cgi
2. Extract ZIP somewhere (e.g. "D:\")
3. Set environment variable `CATALINA_HOME`, point it to Tomcat directory (e.g. `D:\apache-tomcat-9.0.6`)
4. Configure a Tomcat user in `CATALINA_HOME/conf/tomcat-users.xml`: 

    `<user username="tomcat" password="Secret-007" roles="tomcat,manager-gui,manager-script"/>`
   
5. Start Tomcat: `CATALINA_HOME/bin/startup.bat`
6. Open the Tomcat root page in the browser: http://localhost:8080
7. Open the Tomcat manager app (login with the user/password you specified): http://localhost:8080/manager/status
8. List the deployed applications: http://localhost:8080/manager/html/list

# Build the application

To build the application with Gradle:

	gradle

# Deploy the application (Cargo plugin)

## Local (project)
The following command will download (`build/download`), extract (`build/extract`) and start a local Tomcat container, and also deploy the application:

    gradle cargoStartLocal

To stop the container:

    gradle cargoStopLocal

## Remote (existing local or remote Tomcat installation)
Configure the tomcat user and password in the `gradle.properties` (for the Tomcat user you configured).
To deploy the application WAR on the specified Tomcat, use:

	gradle cargoRedeployRemote
	
# Test the endpoints:
- http://localhost:8080/test/example (Servlet)
- http://localhost:8080/test/api/example/weather (Web service)
- http://localhost:8080/test/api/example/properties (Web service)
