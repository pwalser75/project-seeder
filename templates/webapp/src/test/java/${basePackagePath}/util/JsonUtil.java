package ${basePackage}.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ${basePackage}.webservice.config.ObjectMapperContextResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * JSON utility functions
 *
 * @author ${user}
 * @since ${date}
 */
public final class JsonUtil {

    private JsonUtil() {

    }

    /**
     * Convert the given object to JSON using JAXB.
     *
     * @param value value
     * @param <T>   type
     * @return json json
     */
    public static <T> String stringify(T value) {
        if (value == null) {
            return null;
        }
        try {
            StringWriter buffer = new StringWriter();
            objectMapper().writeValue(buffer, value);
            return buffer.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Convert the given JSON back to an object using JAXB.
     *
     * @param type type
     * @param json json
     * @param <T>  type
     * @return object of the given type
     */
    public static <T> T parse(Class<T> type, String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper().readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ObjectMapper objectMapper() {
        return new ObjectMapperContextResolver().getContext(JsonUtil.class);
    }
}
