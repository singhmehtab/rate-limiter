package com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("SlidingWindowCounter")
@Data
@Builder
public class SlidingWindowCounterRedisEntity implements Serializable {

    @Id
    private String identifier;

    private int currentTokensUsed;
    private int previousWindowTokensUsed;
    private long windowStartTime;

}
