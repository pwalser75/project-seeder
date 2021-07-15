package ${basePackage};

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for Example
 */
class ExampleTest {

    @Test
    void testMessage() {
        Example example = new Example();

        assertThat(example.getMessage()).isNotNull();
        assertThat(example.getMessage()).isEqualTo("Hello World");
    }
}
