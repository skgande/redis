package com.sunil.redis.examples;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class AppendToList {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();

        String key = "mylist";
        String value = "hello world";

        commands.rpush(key, value);

        long listSize = commands.llen(key);
        System.out.println("List size: " + listSize);

        connection.close();
        redisClient.shutdown();
    }
}
