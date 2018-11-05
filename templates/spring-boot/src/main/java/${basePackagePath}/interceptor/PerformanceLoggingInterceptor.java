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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Performance logging interceptor, logs performance and result state (ok or exception) for (nested) service calls.<br>
 * Activated by profile
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
 * 15:12:22.316 INFO  [main] | PerformanceLoggingInterceptor - Test.a() → 211.20 ms, self: 51.30 ms
 * &nbsp;&nbsp;&#10551; Test.b() → java.lang.IllegalArgumentException, 134.04 ms, self: 102.04 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.c() &#8594; 11.01 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.c() &#8594; 10.01 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.c() &#8594; 10.95 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Test.d() &#8594; java.lang.ArithmeticException, 0.03 ms
 * &nbsp;&nbsp;&#10551; Test.e() &#8594; 25.86 ms
 * 15:12:22.339 INFO  [main] | PerformanceLoggingInterceptor - Other.x() &#8594; 12.55 ms, self: 2.57 ms
 * &nbsp;&nbsp;&#10551; Other.y() &#8594; 9.98 ms, self: 7.92 ms
 * &nbsp;&nbsp;&nbsp;&nbsp;&#10551; Other.z() &#8594; 2.06 ms
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

        private final static ThreadLocal<PerformanceLoggingContext> current = new ThreadLocal<>();

        private final List<InvocationInfo> invocations = new LinkedList<>();
        private final Deque<InvocationInfo> invocationStack = new LinkedList<>();
        private final Deque<AtomicLong> nestedTime = new LinkedList<>();

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
            nestedTime.push(new AtomicLong());
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
            info.done(time, t != null ? t.getClass().getName() : null);
            info.setNestedTimeNs(nestedTime.pop().get());

            Optional.ofNullable(nestedTime.peek()).ifPresent(x -> x.addAndGet(info.getElapsedTimeNs()));

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
        private long nestedTimeNs;

        public InvocationInfo(int level, String invocation, long startTimeNs) {
            this.level = level;
            this.startTimeNs = startTimeNs;
            this.invocation = invocation;
        }

        public void done(long endTimeNs, String result) {
            this.endTimeNs = endTimeNs;
            this.result = result;
        }

        public long getElapsedTimeNs() {
            return endTimeNs - startTimeNs;
        }

        public void setNestedTimeNs(long timeNs) {
            nestedTimeNs = timeNs;
        }

        @Override
        public String toString() {

            long durationNs = endTimeNs - startTimeNs;

            StringBuilder builder = new StringBuilder();
            if (level > 0) {
                for (int i = 0; i < level; i++) {
                    builder.append("  ");
                }
                builder.append("\u2937 ");
            }

            builder.append(invocation);
            builder.append(" \u2192 ");
            if (result != null) {
                builder.append(result);
                builder.append(", ");
            }
            builder.append(formatTimeMs(durationNs));
            if (nestedTimeNs > 0) {
                builder.append(", self: ");
                builder.append(formatTimeMs(durationNs - nestedTimeNs));
            }
            return builder.toString();
        }

        private String formatTimeMs(long timeNs) {
            return BigDecimal.valueOf(timeNs * 0.000001).setScale(2, RoundingMode.HALF_EVEN) + " ms";
        }
    }
}
