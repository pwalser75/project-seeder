package ${basePackage}.ws.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import ${basePackage}.ws.client.NoteClient;

import javax.ws.rs.client.ClientBuilder;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Rest client configuration, provides the {@link ClientBuilder}
 */
public final class RestClientConfig {

    private RestClientConfig() {

    }

    public static ClientBuilder clientBuilder() {

        try (InputStream in = NoteClient.class.getResourceAsStream("/client-truststore.jks")) {
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(in, "truststore".toCharArray());

            JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
            jsonProvider.setMapper(objectMapper());

            return ClientBuilder.newBuilder()
                    .trustStore(truststore)
                    .property(ClientProperties.CONNECT_TIMEOUT, 1000)
                    .property(ClientProperties.READ_TIMEOUT, 5000)
                    .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                    .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING")
                    .register(jsonProvider);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load client truststore", ex);
        }
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new StdDateFormat());
        mapper.disable(SerializationFeature.CLOSE_CLOSEABLE.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
