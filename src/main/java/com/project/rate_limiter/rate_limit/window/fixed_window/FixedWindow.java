package com.project.rate_limiter.rate_limit.window.fixed_window;

import com.project.rate_limiter.rate_limit.window.IWindowRefillStrategy;

import static com.project.rate_limiter.Constants.MAX_TOKEN_THRESHOLD;

public class FixedWindow{

    private int currentTokens = MAX_TOKEN_THRESHOLD;

    private final IWindowRefillStrategy iWindowRefillStrategy;

    public FixedWindow(IWindowRefillStrategy iWindowRefillStrategy){
        this.iWindowRefillStrategy = iWindowRefillStrategy;
    }

    public synchronized boolean useToken(){
        if(isTokenAvailable()){
            currentTokens--;
            return true;
        }
        return false;
    }

    private boolean isTokenAvailable(){
        if(iWindowRefillStrategy.isWindowRenewed()){
            refill();
        }
        return currentTokens > 0;
    }

    private void refill(){
        currentTokens = (int) iWindowRefillStrategy.refill();
    }

}
