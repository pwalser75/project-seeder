package ${basePackage}.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author pwalser
 * @since 26.10.2018.
 */
@Aspect
@Configuration
public class PerformanceLoggingInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceLoggingInterceptor.class);

    @Around("@within(${basePackage}.interceptor.PerformanceLogging)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();

        String error = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            error = t.getClass().getName();
            throw t;
        } finally {
            long durationNs = System.nanoTime() - startTime;
            logger.info("{} -> {}, {} ms", joinPoint.getSignature().toShortString(), error != null ? error : "ok", formatDuration(durationNs));
        }
    }

    private String formatDuration(long durationNs) {
        return BigDecimal.valueOf(durationNs / 1000000d).setScale(2, RoundingMode.HALF_UP).toString();
    }

}
