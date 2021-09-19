package ${basePackage}.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ${basePackage}.platform.util.ObjectMappers.json;

/**
 * Jackson configuration.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return json();
    }
}