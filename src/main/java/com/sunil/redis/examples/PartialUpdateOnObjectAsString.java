package com.sunil.redis.examples;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class PartialUpdateOnObjectAsString {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();

        System.out.println("------------------------");
        System.out.println("Creating key");
        String key = "mykey";
        String value = "{ \"name\": \"John\", \"age\": 30 }";
        commands.set(key, value);
        System.out.println("Key created");
        System.out.println("Value for key 'mykey' is : " + commands.get("mykey"));
        System.out.println("------------------------");

        int offset = 0;
        String partialValue = "{ \"name\": \"Adam\",";
        int length = partialValue.length();

        String currentValue = commands.get(key);
        String updatedValue = currentValue.substring(0, offset) + partialValue + currentValue.substring(offset + length);
        commands.set(key, updatedValue);

        String retrievedValue = commands.get(key);
        System.out.println("Value after partial update is : " + retrievedValue);
        System.out.println("------------------------");

        connection.close();
        redisClient.shutdown();
    }
}
