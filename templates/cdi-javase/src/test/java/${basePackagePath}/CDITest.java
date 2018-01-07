package ${basePackage};

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${basePackage}.service.WelcomeService;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.util.Set;

/**
 * CDI Test
 */
public class CDITest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testWelcome() {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            WelcomeService welcomeService = container.select(WelcomeService.class).get();
            String message = welcomeService.welcome();

            Assert.assertNotNull(message);
            Assert.assertTrue(message.trim().length() > 0);

            log.info(message);
        }
    }

    @Test
    public void testLookup() {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {

            BeanManager beanManager = CDI.current().getBeanManager();
            Assert.assertNotNull(beanManager);

            Set<Bean<?>> beans = beanManager.getBeans(WelcomeService.class);
            Assert.assertNotNull(beans);
            Assert.assertFalse(beans.isEmpty());
            Bean<?> bean = beans.stream().findFirst().get();
            Assert.assertNotNull(bean);
            WelcomeService welcomeService = (WelcomeService) beanManager.getReference(bean, Object.class, beanManager.createCreationalContext(bean));
            Assert.assertNotNull(welcomeService);

            String message = welcomeService.welcome();

            Assert.assertNotNull(message);
            Assert.assertTrue(message.trim().length() > 0);

            log.info(message);
        }
    }
}
