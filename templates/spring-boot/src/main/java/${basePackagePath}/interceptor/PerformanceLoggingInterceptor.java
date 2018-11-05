package ${basePackage}.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Performance logging interceptor, logs performance and result state (ok or exception) for (nested) service calls.<br> Activated by profile
 * <code>performance-logging</code>, the interceptor will log performance for any
 * <ul>
 * <li>classes annotated with <code>@PerformanceLogging</code></li>
 * <li>Spring <code>@Service</code></li>
 * <li>Spring <code>@Controller</code></li>
 * <li>Spring <code>@RestController</code></li>
 * <li>Spring Data <code>Repository</code></li>
 * </ul>
 * Example log output:
 * <code><pre>
 * 15:32:14.121 INFO  [main] | PerformanceLoggingInterceptor - Test.a() &#8594; ok, 212.73 ms
 * &nbsp;&nbsp;&#10551; Test.b() &#8594; java.lang.IllegalArgumentException, 132.13 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.c() &#8594; ok, 10.97 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.c() &#8594; ok, 9.96 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.c() &#8594; ok, 11.00 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.d() &#8594; java.lang.ArithmeticException, 0.05 ms
 * &nbsp;&nbsp;&#10551; Test.e() &#8594; ok, 25.85 ms
 * 15:32:14.133 INFO  [main] | PerformanceLoggingInterceptor - Other.x() &#8594; ok, 8.49 ms
 * &nbsp;&nbsp;&#10551; Other.y() &#8594; ok, 6.24 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Other.z() &#8594; ok, 2.49 ms
 *
 * </pre></code>
 */
@Aspect
@Configuration
@Profile("performance-logging")
public class PerformanceLoggingInterceptor {

    private static Logger log = LoggerFactory.getLogger(PerformanceLoggingInterceptor.class);

    /**
     * Bind aspect to any Spring @Service, @Controller, @RestController and Repository
     */
    @Around("@within(${basePackage}.interceptor.PerformanceLogging) " +
            "|| @within(org.springframework.stereotype.Service) " +
            "|| @within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController) " +
            "|| this(org.springframework.data.repository.Repository)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String invocation = joinPoint.getSignature().toShortString();
        PerformanceLoggingContext.current().enter(invocation);

        Throwable error = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw error = t;
        } finally {
            PerformanceLoggingContext.current().exit(error);
        }
    }

    static class PerformanceLoggingContext {

        private static ThreadLocal<PerformanceLoggingContext> current = new ThreadLocal<>();

        private List<InvocationInfo> invocations = new LinkedList<>();
        private Deque<InvocationInfo> invocationStack = new LinkedList<>();

        public static PerformanceLoggingContext current() {
            PerformanceLoggingContext context = current.get();
            if (context == null) {
                context = new PerformanceLoggingContext();
                current.set(context);
            }
            return context;
        }

        public void enter(String invocation) {
            long time = System.nanoTime();
            InvocationInfo invocationInfo = new InvocationInfo(invocationStack.size(), invocation, time);
            invocations.add(invocationInfo);
            invocationStack.push(invocationInfo);
        }

        public void exit() {
            exit(null);
        }

        public void exit(Throwable t) {
            long time = System.nanoTime();
            if (invocationStack.isEmpty()) {
                throw new IllegalStateException("No invocation in progress");
            }
            InvocationInfo info = invocationStack.pop();
            info.done(time, t != null ? t.getClass().getName() : "ok");

            if (invocationStack.isEmpty()) {
                log.info(invocations.stream().map(InvocationInfo::toString).collect(Collectors.joining("\n")));
                invocations.clear();
                current.remove();
            }
        }
    }

    private static class InvocationInfo {

        private final int level;
        private final long startTimeNs;
        private long endTimeNs;
        private final String invocation;
        private String result;

        public InvocationInfo(int level, String invocation, long startTimeNs) {
            this.level = level;
            this.startTimeNs = startTimeNs;
            this.invocation = invocation;
        }

        public void done(long endTimeNs, String result) {
            this.endTimeNs = endTimeNs;
            this.result = result;
        }

        @Override
        public String toString() {

            long durationNs = endTimeNs - startTimeNs;
            String duration = BigDecimal.valueOf(durationNs * 0.000001).setScale(2, RoundingMode.HALF_UP) + " ms";

            StringBuilder builder = new StringBuilder();
            if (level > 0) {
                for (int i = 0; i < level; i++) {
                    builder.append("  ");
                }
                builder.append("\u2937 ");
            }

            builder.append(invocation);
            builder.append(" \u2192 ");
            builder.append(result);
            builder.append(", ");
            builder.append(duration);
            return builder.toString();
        }
    }
}
