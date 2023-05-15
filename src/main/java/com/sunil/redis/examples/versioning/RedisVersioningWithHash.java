package com.sunil.redis.examples.versioning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunil.redis.model.Payment;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisHashCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

import java.util.Map;

public class RedisVersioningWithHash {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        // Connect to Redis
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, new ByteArrayCodec()));

        // Create a RedisHashCommands object
        RedisHashCommands<String, byte[]> hashCommands = connection.sync();

        long time = System.currentTimeMillis();
        String key = "ph1";
        Payment payment = new Payment("ph1", "TO_BE_PROCESSED");
        byte[] paymentStream = MAPPER.writeValueAsBytes(payment);
        System.out.println("------------------------------------");
        System.out.println("Creating payment key in redis");
        hashCommands.hset(key, Map.of(String.valueOf(time), paymentStream));
        System.out.println("Payment key " + key + " created in redis");
        System.out.println("------------------------------------");
        String value = MAPPER.readValue(hashCommands.hget(key, String.valueOf(time)), Payment.class).toString();
        System.out.println("From redis --> Payment key : " + key + " and value : " + value);
        System.out.println("------------------------------------");
    }
}
/*
Output
------------------------------------
Creating payment key in redis
Payment key ph1 created in redis
------------------------------------
From redis --> Payment key : ph1 and value : Payment{txnId='ph1', statusCode='TO_BE_PROCESSED'}
------------------------------------

Process finished with exit code 0
 */