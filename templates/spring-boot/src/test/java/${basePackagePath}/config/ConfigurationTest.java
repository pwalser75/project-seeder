package ${basePackage}.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ${basePackage}.config.ExampleProperties;

/**
 * Test for {@link ExampleProperties}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationTest {

    @Autowired
    private ExampleProperties exampleProperties;

    @Test
    public void testConfigurationProperties() {

        Assert.assertNotNull(exampleProperties);
        Assert.assertTrue(exampleProperties.getA() == 123);
        Assert.assertTrue(exampleProperties.getB() == 123.456);
        Assert.assertTrue(exampleProperties.isC());
        Assert.assertEquals("Test", exampleProperties.getD());

        Assert.assertNotNull(exampleProperties.getE());
        Assert.assertEquals(3, exampleProperties.getE().size());
        Assert.assertTrue(exampleProperties.getE().contains("ONE"));
        Assert.assertTrue(exampleProperties.getE().contains("TWO"));
        Assert.assertTrue(exampleProperties.getE().contains("THREE"));

        Assert.assertNotNull(exampleProperties.getF());
        Assert.assertEquals(3, exampleProperties.getF().size());
        Assert.assertEquals("first", exampleProperties.getF().get(0));
        Assert.assertEquals("second", exampleProperties.getF().get(1));
        Assert.assertEquals("third", exampleProperties.getF().get(2));
    }
}
