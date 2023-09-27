package com.project.rate_limiter.rate_limit.window;

public interface IWindowRefillStrategy {

    double refill();

    boolean isWindowRenewed();

    boolean noTokenUsedInPreviousWindow();

    default long getTimeInNano(long timeInMS){
        return timeInMS * 1000000L;
    }

}
