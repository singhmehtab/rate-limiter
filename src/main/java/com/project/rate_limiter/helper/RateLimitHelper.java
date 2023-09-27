package com.project.rate_limiter.helper;

import com.project.rate_limiter.rate_limit.IRateLimiter;
import com.project.rate_limiter.rate_limit.window.fixed_window.FixedWindowRateLimiter;
import com.project.rate_limiter.rate_limit.window.sliding_window_counter.SlidingWindowCounterRateLimiter;
import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.SlidingWindowCounterRateLimiterRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitHelper {

    private final IRateLimiter iRateLimiter;

    @Autowired
    public RateLimitHelper(SlidingWindowCounterRateLimiterRedis tokenBucketRateLimiter){
        this.iRateLimiter = tokenBucketRateLimiter;
    }

    public boolean isAllowed(String ipAddress){
        return iRateLimiter.isAllowed(ipAddress);
    }

}
