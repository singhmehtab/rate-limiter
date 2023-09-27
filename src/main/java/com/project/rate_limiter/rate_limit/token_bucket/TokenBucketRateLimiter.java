package com.project.rate_limiter.rate_limit.token_bucket;

import com.project.rate_limiter.Constants;
import com.project.rate_limiter.rate_limit.IRateLimiter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TokenBucketRateLimiter implements IRateLimiter {

    public HashMap<String, TokenBucket> userTokenBucketMap = new HashMap<>();

    @Override
    public boolean isAllowed(String ipAddress) {
        if(!userTokenBucketMap.containsKey(ipAddress)){
            userTokenBucketMap.put(ipAddress, new TokenBucket(new TimeIntervalBasedBucketRefill(Constants.TOKEN_BUCKET_TOKEN_INCREASE_TIME, Constants.TOKEN_BUCKET_TOKEN_INCREASE_UNIT)));
        }
        return userTokenBucketMap.get(ipAddress).useToken();
    }
}
