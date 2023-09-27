package com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis;

import com.project.rate_limiter.Constants;
import com.project.rate_limiter.rate_limit.IRateLimiter;
import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_model.SlidingWindowCounterRedisEntity;
import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_service.SlidingWindowCounterRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SlidingWindowCounterRateLimiterRedis implements IRateLimiter {

    private final SlidingWindowCounterRedisService slidingWindowCounterRedisService;

    @Autowired
    public SlidingWindowCounterRateLimiterRedis(SlidingWindowCounterRedisService slidingWindowCounterRedisService){
        this.slidingWindowCounterRedisService = slidingWindowCounterRedisService;
    }
    @Override
    public boolean isAllowed(String ipAddress) {
        SlidingWindowCounterRedisEntity slidingWindowCounterRedisEntity = slidingWindowCounterRedisService.findById(ipAddress);
        if(Objects.isNull(slidingWindowCounterRedisEntity)) {
            slidingWindowCounterRedisEntity = SlidingWindowCounterRedisEntity.builder()
                    .identifier(ipAddress)
                    .currentTokensUsed(0)
                    .previousWindowTokensUsed(0)
                    .windowStartTime(System.nanoTime())
                    .build();
            slidingWindowCounterRedisEntity = slidingWindowCounterRedisService.createRecord(slidingWindowCounterRedisEntity);
        }
        SlidingWindowCounterRefillRedis slidingWindowCounterRefillRedis = new SlidingWindowCounterRefillRedis(Constants.WINDOW_SIZE_IN_MS, slidingWindowCounterRedisEntity.getWindowStartTime());
        SlidingWindowCounterRedis slidingWindowCounterRedis = new SlidingWindowCounterRedis(slidingWindowCounterRefillRedis, slidingWindowCounterRedisEntity);
        boolean result = slidingWindowCounterRedis.useToken();
        slidingWindowCounterRedisEntity.setWindowStartTime(slidingWindowCounterRefillRedis.getWindowStartTime());
        slidingWindowCounterRedisService.updateRecord(slidingWindowCounterRedisEntity);
        return result;
    }
}
