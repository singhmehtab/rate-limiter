package com.project.rate_limiter.rate_limit;

public interface IRateLimiter {

    boolean isAllowed(String ipAddress);

}
