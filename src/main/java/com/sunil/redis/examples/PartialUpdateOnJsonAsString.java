package com.sunil.redis.examples;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.Map;

public class PartialUpdateOnJsonAsString {
    private static final String TXN_ID = "P3";

    public static void main(String[] args) throws Exception {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();

        System.out.println("--------------------------------------------");
        System.out.println("     Connection successful                  ");
        System.out.println("--------------------------------------------");

        // Create a key in redis.
        create(commands);

        // Get key from redis.
        readKey(commands);

        // Partial update on the value at parent level attribute.
        update(commands);

        // Get key from redis.
        readKey(commands);

        connection.close();
        redisClient.shutdown();
    }

    private static void update(RedisCommands redisCommands) {
        System.out.println("    Update status alone on the payment P3   ");
        redisCommands.hset(TXN_ID, Map.of( "statusCode", "ACCEPTED"));
        System.out.println("--------------------------------------------");
    }

    private static void readKey(RedisCommands redisCommands) {
        System.out.println("         Get P3 from Redis                  ");
        System.out.println("P3 from redis is : " + redisCommands.hgetall(TXN_ID).toString());
        System.out.println("--------------------------------------------");
    }

    private static void create(RedisCommands redisCommands) {
        System.out.println("        Writing P3 into Redis               ");
        redisCommands.hmset(TXN_ID, Map.of("txnId", TXN_ID, "statusCode", "TO_BE_PROCESSED"));
        System.out.println("--------------------------------------------");
    }
}
