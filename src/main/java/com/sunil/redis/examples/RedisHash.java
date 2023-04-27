package com.sunil.redis.examples;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisHashCommands;

import java.util.HashMap;
import java.util.Map;

public class RedisHash {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        // Connect to Redis
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        // Create a RedisHashCommands object
        RedisHashCommands<String, String> hashCommands = connection.sync();

        // Define the hash key and fields to update
        String key = "user:123";
        Map<String, String> fieldValues = new HashMap<>();
        fieldValues.put("name", "John");
        fieldValues.put("email", "john@example.com");

        // Update the fields in the hash
        hashCommands.hmset(key, fieldValues);
        Map<String, String> fieldsMap = hashCommands.hgetall(key);
        System.out.println(fieldsMap);

        hashCommands.hset(key, "name", "Joe");
        Map<String, String> updatedFieldsMap = hashCommands.hgetall(key);
        System.out.println(updatedFieldsMap);

        // Close the Redis connection
        connection.close();
        redisClient.shutdown();
    }
}
