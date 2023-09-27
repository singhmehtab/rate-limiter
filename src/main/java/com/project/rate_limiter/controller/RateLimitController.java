package com.project.rate_limiter.controller;

import com.project.rate_limiter.helper.RateLimitHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RateLimitController {

    private final RateLimitHelper rateLimitHelper;

    @Autowired
    public RateLimitController(RateLimitHelper rateLimitHelper){
        this.rateLimitHelper = rateLimitHelper;
    }

    @RequestMapping(method = RequestMethod.GET, value = "unlimited")
    public String unlimited(){
        return "Unlimited! Let's Go!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "limited")
    public String limited(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        if(rateLimitHelper.isAllowed(httpServletRequest.getRemoteAddr())){
            return "Limited, don't over use me!";
        }
        else{
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return null;
        }
    }

}
