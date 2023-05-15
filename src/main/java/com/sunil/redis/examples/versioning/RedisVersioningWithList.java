package com.sunil.redis.examples.versioning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunil.redis.model.Payment;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

public class RedisVersioningWithList {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        // String to byte[]
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, new ByteArrayCodec()));

        RedisCommands<String, byte[]> commands = connection.sync();

        String key = "plist";
        Payment payment = new Payment("plist", "TO_BE_PROCESSED");
        byte[] paymentStream = MAPPER.writeValueAsBytes(payment);
        commands.lpush(key, paymentStream);

        payment = new Payment("plist", "COMPLETED");
        paymentStream = MAPPER.writeValueAsBytes(payment);
        commands.lpush(key, paymentStream);

        byte[] valueStream = commands.lindex(key, 0);
        String value = MAPPER.readValue(valueStream, Payment.class).toString();
        System.out.println("From redis --> Payment key : " + key + " and value : " + value);
    }
}
