package ${basePackage};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example class
 */
public class Example {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        new Example();
    }

    public Example() {
        log.info(getMessage());
    }

    public String getMessage() {
        return "Hello World";
    }
}
