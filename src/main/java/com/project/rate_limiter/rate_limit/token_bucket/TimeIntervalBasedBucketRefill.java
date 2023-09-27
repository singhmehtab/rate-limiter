package com.project.rate_limiter.rate_limit.token_bucket;

public class TimeIntervalBasedBucketRefill implements IBucketRefillStrategy {

    // Time after which token should refill. Time is in millisecond.
    private final int tokenIncreaseTime;

    // Number of tokens to be filled after token increase time
    private final int tokenIncreaseUnit;

    private long lastBucketRefillTime;

    public TimeIntervalBasedBucketRefill(int tokenIncreaseTime, int tokenIncreaseUnit){
        this.tokenIncreaseTime = tokenIncreaseTime;
        this.tokenIncreaseUnit = tokenIncreaseUnit;
        this.lastBucketRefillTime = System.nanoTime();
    }


    @Override
    public int refill() {
        long timeNow = System.nanoTime();
        double timeUnitsElapsedInMS = ((double) (timeNow - lastBucketRefillTime)) /1000000.0;
        double unitsPassedUntilRefill = timeUnitsElapsedInMS/tokenIncreaseTime;
        if(unitsPassedUntilRefill >= 1) lastBucketRefillTime = timeNow;
        return (int) unitsPassedUntilRefill * tokenIncreaseUnit;
    }
}
