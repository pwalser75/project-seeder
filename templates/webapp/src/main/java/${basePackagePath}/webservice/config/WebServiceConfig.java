package ${basePackage}.webservice.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Web service configuration (root path for web services)
 */
@ApplicationPath("/api")
public class WebServiceConfig extends Application {
}
