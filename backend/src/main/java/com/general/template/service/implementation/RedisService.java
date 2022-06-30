package com.general.template.service.implementation;

import com.general.template.domain.Score;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class RedisService {
    private final RedisTemplate redisTemplate;

    public <T, V> void put(T key, V value){
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
    }

    public <T> void append(T key, String value){
        redisTemplate.opsForValue().append(key, value);
    }

    public <T, V> boolean setIfAbsent(T key, V value){
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public <T> Object get(T key){
        return redisTemplate.opsForValue().get(key);
    }

    public <T> void delete(T key){
        redisTemplate.delete(key);
    }

    public <T, U, V> void putProperty(T key, U hashKey, V value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public <T, U, V> void putFullObject(T key, Map<U, V> maps){
        redisTemplate.opsForHash().putAll(key,maps);
    }

    public <T, V> Object getProperty(T key,V field){
        return  redisTemplate.opsForHash().get(key,field);
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public <T> Object getFromListByIndex(T key, Long idx) {
        return redisTemplate.opsForList().index(key, idx);
    }

    public List<Object> getFromListByRange(String key, Integer from, Integer to) {
        return redisTemplate.opsForList().range(key, from, to);
    }

    public <T, V> Object leftPushToList(T key, V val, Integer expiry) {
        redisTemplate.expire(key, expiry, TimeUnit.SECONDS);
        return redisTemplate.opsForList().leftPush(key, val);
    }

    public <T, V> Object leftPushAllToList(T key, List<V> val, Integer expiry) {
        redisTemplate.expire(key, expiry, TimeUnit.SECONDS);
        return redisTemplate.opsForList().leftPushAll(key, val);
    }

    public <T, V> Object rightPushToList(T key, V val) {
        return redisTemplate.opsForList().rightPush(key, val);
    }
}
