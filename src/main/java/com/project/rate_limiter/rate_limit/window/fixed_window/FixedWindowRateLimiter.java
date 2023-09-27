package com.project.rate_limiter.rate_limit.window.fixed_window;

import com.project.rate_limiter.Constants;
import com.project.rate_limiter.rate_limit.IRateLimiter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FixedWindowRateLimiter implements IRateLimiter {

    private HashMap<String, FixedWindow> userTokenMap = new HashMap<>();
    @Override
    public boolean isAllowed(String ipAddress) {
        if(!userTokenMap.containsKey(ipAddress))userTokenMap.put(ipAddress, new FixedWindow(new FixedWindowBasedRefill(Constants.WINDOW_SIZE_IN_MS)));
        return userTokenMap.get(ipAddress).useToken();
    }
}
