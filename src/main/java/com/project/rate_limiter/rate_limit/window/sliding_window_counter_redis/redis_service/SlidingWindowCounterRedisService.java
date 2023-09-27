package com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_service;

import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_model.SlidingWindowCounterRedisEntity;
import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_repository.SlidingWindowCounterRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class SlidingWindowCounterRedisService {

    private final SlidingWindowCounterRedisRepository slidingWindowCounterRedisRepository;

    @Autowired public SlidingWindowCounterRedisService(SlidingWindowCounterRedisRepository slidingWindowCounterRedisRepository){
        this.slidingWindowCounterRedisRepository = slidingWindowCounterRedisRepository;
    }

    public SlidingWindowCounterRedisEntity createRecord(SlidingWindowCounterRedisEntity slidingWindowCounterRedisEntity){
        log.info("Creating record for id " + slidingWindowCounterRedisEntity.getIdentifier());
        return slidingWindowCounterRedisRepository.save(slidingWindowCounterRedisEntity);
    }

    public void updateRecord(SlidingWindowCounterRedisEntity slidingWindowCounterRedisEntity){
        log.info("Updating record for id " + slidingWindowCounterRedisEntity.getIdentifier());
        Optional<SlidingWindowCounterRedisEntity> slidingWindowCounter = slidingWindowCounterRedisRepository.findById(slidingWindowCounterRedisEntity.getIdentifier());
        slidingWindowCounter.ifPresentOrElse(slidingWindow ->
            {
                slidingWindow.setCurrentTokensUsed(slidingWindowCounterRedisEntity.getCurrentTokensUsed());
                slidingWindow.setPreviousWindowTokensUsed(slidingWindowCounterRedisEntity.getPreviousWindowTokensUsed());
                slidingWindow.setWindowStartTime(slidingWindowCounterRedisEntity.getWindowStartTime());
                slidingWindowCounterRedisRepository.save(slidingWindow);
            }, () -> {
                throw new NoSuchElementException("No element with ip address " + slidingWindowCounterRedisEntity.getIdentifier() + " exists in the redis records");
            });
    }

    public SlidingWindowCounterRedisEntity findById(String identifier) {
        log.info("Fetching record for id " + identifier);
        return slidingWindowCounterRedisRepository.findById(identifier).orElse(null);
    }
}
