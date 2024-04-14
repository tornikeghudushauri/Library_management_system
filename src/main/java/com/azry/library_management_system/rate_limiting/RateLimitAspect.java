package com.azry.library_management_system.rate_limiting;

import com.azry.library_management_system.exception.RateLimitException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {

    public static final String ERROR_MESSAGE = "You exceeded rate limit of requests at endpoint %s from IP %s! Please try again after %d seconds!";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${app.rate.limit}")
    private int rateLimit;

    @Value("${app.limit.duration}")
    private long limitDuration;

    /**
     * Executed after each call of a method annotated with {@link RateLimitProtection}.
     * Counts calls per remote address. Calls before {@link #limitDuration} milliseconds will not be taken into account.
     * If there have been more than {@link #rateLimit} calls within {@link #limitDuration} milliseconds from
     * a remote address, a {@link RateLimitException} will be thrown.
     *
     * @throws RateLimitException if rate limit for a given remote address has been exceeded
     */
    @Before("@annotation(com.azry.library_management_system.rate_limiting.RateLimitProtection)")
    public void rateLimitProtection() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(key, new ArrayList<>());
        requestCounts.get(key).add(currentTime);
        clearRequestCounter(currentTime);

        if (requestCounts.get(key).size() > rateLimit) {
            throw new RateLimitException(
                    String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key, limitDuration / 1000)
            );
        }
    }

    private void clearRequestCounter(final long currentTime) {
        requestCounts.values().forEach(l -> l.removeIf(t -> isBefore(currentTime, t)));
    }

    private boolean isBefore(final long currentTime, final long timeToCheck) {
        return currentTime - timeToCheck > limitDuration;
    }
}