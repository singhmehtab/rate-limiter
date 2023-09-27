package com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis;

import com.project.rate_limiter.rate_limit.window.IWindowRefillStrategy;
import lombok.Getter;

public class SlidingWindowCounterRefillRedis implements IWindowRefillStrategy {

    @Getter
    private long windowStartTime;

    private final int windowSizeInMS;

    public SlidingWindowCounterRefillRedis(int windowSizeInMS, long windowStartTime){
        this.windowSizeInMS = windowSizeInMS;
        this.windowStartTime = windowStartTime;
    }

    // refill here means the ratio of requests to be considered from the previous window.
    // This ratio would not be used to actually refill 0:0:0:0:0:0:0:1the tokens in window, but to add to the current tokens used and compare with threshold.
    @Override
    public double refill() {
        if(isWindowRenewed()){
            this.windowStartTime = getNextWindowTime();
        }
        return getPreviousWindowRequiredRatio();
    }

    @Override
    public boolean isWindowRenewed() {
        long nextWindowTime = getNextWindowTime();
        return System.nanoTime() >= nextWindowTime;
    }

    public boolean noTokenUsedInPreviousWindow(){
        return System.nanoTime() >= (windowStartTime + getTimeInNano(windowSizeInMS) * 2);
    }

    private long getNextWindowTime(){
        if(windowStartTime + 2*getTimeInNano(windowSizeInMS) <= System.nanoTime()){
            return System.nanoTime();
        }
        return windowStartTime + getTimeInNano(windowSizeInMS);
    }

    private double getCurrentWindowUsePercentage(){
        long currentTime = System.nanoTime();
        return (double) (currentTime - windowStartTime) / (windowSizeInMS * 1000000L);
    }

    private double getPreviousWindowRequiredRatio(){
        return 1 - getCurrentWindowUsePercentage();
    }

}
