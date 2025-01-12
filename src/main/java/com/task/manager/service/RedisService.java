package com.task.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // Store data in Redis
    public void set(String key, Object value, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.MINUTES);
            log.info("Data saved in Redis with key: {}", key);
        } catch (Exception e) {
            log.error("Error saving data to Redis: {}", e.getMessage());
        }
    }


    // Retrieve data from Redis
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            String jsonValue = (String) redisTemplate.opsForValue().get(key);
            if (Objects.nonNull(jsonValue)) {
                return objectMapper.readValue(jsonValue, typeReference);
            }
        } catch (Exception e) {
            log.error("Error retrieving data from Redis: {}", e.getMessage());
        }
        return null;
    }


}
