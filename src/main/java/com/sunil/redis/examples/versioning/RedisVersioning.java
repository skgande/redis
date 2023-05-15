package com.sunil.redis.examples.versioning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunil.redis.model.Payment;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.RedisClient;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

public class RedisVersioning {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        // String to byte[]
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, new ByteArrayCodec()));
        RedisCommands<String, byte[]> commands = connection.sync();

        long time = System.currentTimeMillis();
        String key = "p1:" + time;
        Payment payment = new Payment("p1", "TO_BE_PROCESSED");
        byte[] paymentStream = MAPPER.writeValueAsBytes(payment);
        System.out.println("------------------------------------");
        System.out.println("Creating payment key in redis");
        commands.set(key, paymentStream);
        System.out.println("Payment key " + key + " created in redis");
        System.out.println("------------------------------------");
        String value = MAPPER.readValue(commands.get(key), Payment.class).toString();
        System.out.println("From redis --> Payment key : " + key + " and value : " + value);
        System.out.println("------------------------------------");
        String prefix = "p1:";
        ScanArgs scanArgs = ScanArgs.Builder.matches(prefix + "*").limit(100); // Limit the number of keys to retrieve

        String cursor = "0";
        do {
            KeyScanCursor<String> scanCursor = commands.scan(scanArgs);
            cursor = scanCursor.getCursor();

            for (String prefixKey : scanCursor.getKeys()) {
                // Get the value for each key
                byte[] keyValue = commands.get(prefixKey);
                System.out.println("Key: " + prefixKey + ", Value: " + MAPPER.readValue(keyValue, Payment.class).toString());
            }
        } while (!cursor.equals("0"));
    }
}
// Output
/*
------------------------------------
        Creating payment key in redis
        Payment key p1:1684129829428 created in redis
        ------------------------------------
        From redis --> Payment key : p1:1684129829428 and value : Payment{txnId='p1', statusCode='TO_BE_PROCESSED'}
        ------------------------------------
        Key: p1:1684129829428, Value: Payment{txnId='p1', statusCode='TO_BE_PROCESSED'}
        Key: p1:1684129392649, Value: Payment{txnId='p1', statusCode='TO_BE_PROCESSED'}
        Key: p1:1684129487763, Value: Payment{txnId='p1', statusCode='TO_BE_PROCESSED'}
        Key: p1:1684129448933, Value: Payment{txnId='p1', statusCode='TO_BE_PROCESSED'}
        Key: p1:1684129806766, Value: Payment{txnId='p1', statusCode='TO_BE_PROCESSED'}

        Process finished with exit code 0
 */