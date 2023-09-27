package com.project.rate_limiter.rate_limit.token_bucket;

public interface IBucketRefillStrategy {

    int refill();

}
