package ${basePackage}.webservice.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Base configuration for JAX-RS web services (defines the base @ApplicationPath)
 *
 * @author ${user}
 * @since ${date}
 */
@ApplicationPath("/api")
public class WebServiceConfig extends Application {
}
