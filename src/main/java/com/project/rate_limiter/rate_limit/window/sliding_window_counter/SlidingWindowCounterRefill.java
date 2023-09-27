package com.project.rate_limiter.rate_limit.window.sliding_window_counter;

import com.project.rate_limiter.rate_limit.window.IWindowRefillStrategy;

public class SlidingWindowCounterRefill implements IWindowRefillStrategy {

    private long windowStartTime = System.nanoTime();

    private final int windowSizeInMS;

    public SlidingWindowCounterRefill(int windowSizeInMS){
        this.windowSizeInMS = windowSizeInMS;
    }

    // refill here means the ratio of requests to be considered from the previous window.
    // This ratio would not be used to actually refill the tokens in window, but to add to the current tokens used and compare with threshold.
    @Override
    public double refill() {
        if(isWindowRenewed()){
            windowStartTime = getNextWindowTime();
        }
        return getPreviousWindowRequiredRatio();
    }

    @Override
    public boolean isWindowRenewed() {
        long nextWindowTime = getNextWindowTime();
        return System.nanoTime() >= nextWindowTime;
    }

    @Override
    public boolean noTokenUsedInPreviousWindow() {
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
        return (double) (currentTime - windowStartTime) / getTimeInNano(windowSizeInMS);
    }

    private double getPreviousWindowRequiredRatio(){
        return 1 - getCurrentWindowUsePercentage();
    }
}
