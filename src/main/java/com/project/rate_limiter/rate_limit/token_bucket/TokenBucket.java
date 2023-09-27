package com.project.rate_limiter.rate_limit.token_bucket;

import com.project.rate_limiter.Constants;

public class TokenBucket {

    private int currentTokens;

    private final IBucketRefillStrategy iBucketRefillStrategy;

    public TokenBucket(IBucketRefillStrategy iBucketRefillStrategy){
        this.currentTokens = Constants.MAX_TOKEN_THRESHOLD;
        this.iBucketRefillStrategy = iBucketRefillStrategy;
    }

    public synchronized boolean useToken(){
        if(isTokenAvailable()){
            this.currentTokens--;
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isTokenAvailable(){
        if(currentTokens > 0){
            return true;
        }
        else{
            refillTokens(iBucketRefillStrategy.refill());
            return currentTokens > 0;
        }
    }

    private synchronized void refillTokens(int numberOfTokens){
        this.currentTokens = Math.min(numberOfTokens + currentTokens, Constants.MAX_TOKEN_THRESHOLD);
    }

}
