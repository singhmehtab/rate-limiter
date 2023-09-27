package com.project.rate_limiter.rate_limit.window.sliding_window_counter;

import com.project.rate_limiter.Constants;
import com.project.rate_limiter.rate_limit.window.IWindowRefillStrategy;

public class SlidingWindowCounter {

    private int currentTokensUsed = 0;

    private final IWindowRefillStrategy iWindowRefillStrategy;

    private int previousWindowTokensUsed = 0;

    public SlidingWindowCounter(IWindowRefillStrategy iWindowRefillStrategy){
        this.iWindowRefillStrategy = iWindowRefillStrategy;
    }

    public synchronized boolean useToken(){
        if(isTokenAvailable()){
            currentTokensUsed++;
            return true;
        }
        return false;
    }

    private boolean isTokenAvailable(){
        if(iWindowRefillStrategy.isWindowRenewed()){
            if(iWindowRefillStrategy.noTokenUsedInPreviousWindow()){
                previousWindowTokensUsed = 0;
            }
            else {
                previousWindowTokensUsed = currentTokensUsed;
            }
            currentTokensUsed = 0;
        }
        double previousWindowRatio = iWindowRefillStrategy.refill();
        return (currentTokensUsed + (previousWindowRatio * previousWindowTokensUsed)) < Constants.MAX_TOKEN_THRESHOLD;
    }

}
