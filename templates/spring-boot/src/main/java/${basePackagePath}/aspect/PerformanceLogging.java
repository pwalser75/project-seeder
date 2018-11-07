package ${basePackage}.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for classes/methods whose performance should be logged.
 *
 * @author pwalser
 * @since 26.10.2018.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceLogging {
}
