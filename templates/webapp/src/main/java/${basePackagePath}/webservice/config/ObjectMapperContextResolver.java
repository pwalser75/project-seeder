package ${basePackage}.webservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Jackson {@link ObjectMapper} configuration, adding support for Java8 Date/Time and ISO-8601 format.
 *
 * @author ${user}
 * @since ${date}
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}