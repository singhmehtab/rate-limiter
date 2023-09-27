package com.project.rate_limiter.rate_limit.window.fixed_window;

import com.project.rate_limiter.rate_limit.window.IWindowRefillStrategy;

import static com.project.rate_limiter.Constants.MAX_TOKEN_IN_WINDOW;

public class FixedWindowBasedRefill implements IWindowRefillStrategy {

    private long windowStartTime = System.nanoTime();

    private final int windowSizeInMS;

    public FixedWindowBasedRefill(int windowSizeInMS){
        this.windowSizeInMS = windowSizeInMS;
    }
    @Override
    public double refill() {
        if(isWindowRenewed()){
            windowStartTime = getNextWindowTime();
            return MAX_TOKEN_IN_WINDOW;
        }
        return 0;
    }

    @Override
    public boolean isWindowRenewed() {
        long currentTime = System.nanoTime();
        return currentTime >= getNextWindowTime();
    }

    // Used to check if the previous windows was totally unused
    @Override
    public boolean noTokenUsedInPreviousWindow() {
        return System.nanoTime() >= (windowStartTime + getTimeInNano(windowSizeInMS)*2);
    }

    private long getNextWindowTime(){
        return windowStartTime + getTimeInNano(windowSizeInMS);
    }
}
