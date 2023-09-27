package com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_repository;

import com.project.rate_limiter.rate_limit.window.sliding_window_counter_redis.redis_model.SlidingWindowCounterRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlidingWindowCounterRedisRepository extends CrudRepository<SlidingWindowCounterRedisEntity, String> {
}
