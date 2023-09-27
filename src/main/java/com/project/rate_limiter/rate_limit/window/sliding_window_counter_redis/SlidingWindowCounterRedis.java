package com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis;

import com.project.rate_limiter.Constants;
import com.project.rate_limiter.rate_limit.window.IWindowRefillStrategy;
import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_model.SlidingWindowCounterRedisEntity;

public class SlidingWindowCounterRedis {
    private final SlidingWindowCounterRedisEntity slidingWindowCounterRedis;

    private final IWindowRefillStrategy iWindowRefillStrategy;


    public SlidingWindowCounterRedis(IWindowRefillStrategy iWindowRefillStrategy, SlidingWindowCounterRedisEntity slidingWindowCounterRedis){
        this.iWindowRefillStrategy = iWindowRefillStrategy;
        this.slidingWindowCounterRedis = slidingWindowCounterRedis;
    }

    public synchronized boolean useToken(){
        if(isTokenAvailable()){
            this.slidingWindowCounterRedis.setCurrentTokensUsed(this.slidingWindowCounterRedis.getCurrentTokensUsed() + 1);
            return true;
        }
        return false;
    }

    private boolean isTokenAvailable(){
        if(iWindowRefillStrategy.isWindowRenewed()){
            if(iWindowRefillStrategy.noTokenUsedInPreviousWindow()){
                this.slidingWindowCounterRedis.setPreviousWindowTokensUsed(0);
            }
            else {
                this.slidingWindowCounterRedis.setPreviousWindowTokensUsed(this.slidingWindowCounterRedis.getCurrentTokensUsed());
            }
            this.slidingWindowCounterRedis.setCurrentTokensUsed(0);
        }
        double previousWindowRatio = iWindowRefillStrategy.refill();
        return (this.slidingWindowCounterRedis.getCurrentTokensUsed() + (previousWindowRatio * this.slidingWindowCounterRedis.getPreviousWindowTokensUsed())) < Constants.MAX_TOKEN_THRESHOLD;
    }

}
