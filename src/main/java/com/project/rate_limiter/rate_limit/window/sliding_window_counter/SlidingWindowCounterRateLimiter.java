package com.project.rate_limiter.rate_limit.window.sliding_window_counter;

import com.project.rate_limiter.Constants;
import com.project.rate_limiter.rate_limit.IRateLimiter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SlidingWindowCounterRateLimiter implements IRateLimiter {

    private HashMap<String, SlidingWindowCounter> userTokenMap = new HashMap<>();
    @Override
    public boolean isAllowed(String ipAddress) {
        if(!userTokenMap.containsKey(ipAddress)) userTokenMap.put(ipAddress, new SlidingWindowCounter(new SlidingWindowCounterRefill(Constants.WINDOW_SIZE_IN_MS)));
        return userTokenMap.get(ipAddress).useToken();
    }
}
