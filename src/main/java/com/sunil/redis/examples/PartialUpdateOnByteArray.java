package com.sunil.redis.examples;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

public class PartialUpdateOnByteArray {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, new ByteArrayCodec()));
        RedisCommands<String, byte[]> commands = connection.sync();

        String key = "key";
        byte[] value = "hello world".getBytes();
        int offset = 6;
        byte[] partialValue = "Redis".getBytes();

        commands.set(key, value);
        System.out.println("Before partial update: " + new String(commands.get(key)));
        commands.setrange(key, offset, partialValue);

        byte[] updatedValue = commands.get(key);
        System.out.println("After partial update: " + new String(updatedValue));

        connection.close();
        redisClient.shutdown();
    }
}
