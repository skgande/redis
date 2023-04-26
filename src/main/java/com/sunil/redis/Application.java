package com.sunil.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunil.redis.model.Payment;
import com.sunil.redis.model.RelatedMessage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

import java.util.Set;

// Enrichment flow when P2 event arrived which is related to P1.
public class Application {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        RedisClient redisClient = RedisClient.create("redis://default:cfO0tBoJ4GVADP7Uzk7d5tAkRvp2bwTW@redis-13913.c114.us-east-1-4.ec2.cloud.redislabs.com:13913");
        // String to byte[]
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, new ByteArrayCodec()));
        RedisCommands<String, byte[]> commands = connection.sync();

        // String to String
        StatefulRedisConnection<String, String> connectionString = redisClient.connect();
        RedisCommands<String, String> commandsString = connectionString.sync();

        // Step1: Create payment in redis.
        createPaymentInRedis(commands);

        // Step2: Create related message in redis.
        RelatedMessage relatedMessage = createRelatedMessageInRedis(commands);

        // Step3: Create an association between payment and related message ids.
        // Ex: PRM_P1 (association key) --> P1_PRM (related message key)
        // In this case PRM_redisP1 --> redisP1_PRM
        String key = "PRM_" + relatedMessage.getRelatedObjectId();
        commandsString.sadd(key, relatedMessage.getRelatedObjectId() + "_PRM");
        commandsString.sadd(key, relatedMessage.getRelatedObjectId() + "_PRM_Dummy1");
        commandsString.sadd(key, relatedMessage.getRelatedObjectId() + "_PRM_Dummy2");
        commandsString.sadd(key, relatedMessage.getRelatedObjectId() + "_PRM_Dummy3");
        long setSize = commandsString.scard(key);
        System.out.println("For the association key " + key + " set size: " + setSize);
        System.out.println("From redis --> Payment to related message association -> key: " + key + " and value : " + commandsString.smembers(key));
        System.out.println("------------------------------------");

        System.out.println("Deriving entire graph for redisP2");
        Payment payment = MAPPER.readValue(commands.get("redisP2"), Payment.class);
        System.out.println("Payment object : " + payment.toString());
        Set<String> relatedMessageIdSet = commandsString.smembers("PRM_redisP1");

        for (String relatedMessageKey: relatedMessageIdSet) {
            byte[] relateMessageByteStream = commands.get(relatedMessageKey);
            if(relateMessageByteStream != null) {
                RelatedMessage paymentRelatedMessage = MAPPER.readValue(commands.get(relatedMessageKey), RelatedMessage.class);
                System.out.println("Related message for the key : " + relatedMessageKey + " and value is : " + paymentRelatedMessage.toString());
            } else {
                System.out.println("Related message info not found for the key : " + relatedMessageKey);
            }
        }
        System.out.println("------------------------------------");

        // Close redis connections.
        connection.close();
        connectionString.close();
        redisClient.shutdown();
    }

    // Key for payment P2 is txnId. In this case redisP2
    private static void createPaymentInRedis(RedisCommands<String, byte[]> commands) throws Exception {
        Payment payment = new Payment("redisP2", "TO_BE_PROCESSED");
        byte[] paymentStream = MAPPER.writeValueAsBytes(payment);
        System.out.println("------------------------------------");
        System.out.println("Creating payment key in redis");
        commands.set(payment.getTxnId(), paymentStream);
        System.out.println("Payment key " + payment.getTxnId() + " created in redis");
        System.out.println("------------------------------------");
        String value = MAPPER.readValue(commands.get(payment.getTxnId()), Payment.class).toString();
        System.out.println("From redis --> Payment key : " + payment.getTxnId() + " and value : " + value);
        System.out.println("------------------------------------");
        commands.mget(payment.getTxnId(), payment.getStatusCode());
    }

    // Key for related message P2 is relatedObjectId_PRM. In this case redisP1_PRM
    private static RelatedMessage createRelatedMessageInRedis(RedisCommands<String, byte[]> commands) throws Exception {
        RelatedMessage relatedMessage = new RelatedMessage("redisP2", "redisP1");
        byte[] relatedMessageStream = MAPPER.writeValueAsBytes(relatedMessage);

        System.out.println("Creating related message key in redis");
        String relatedMessageKey = relatedMessage.getRelatedObjectId() + "_PRM";
        commands.set(relatedMessageKey, relatedMessageStream);

        System.out.println("Related message key " + relatedMessageKey + " created in redis");
        System.out.println("------------------------------------");
        String value = MAPPER.readValue(commands.get(relatedMessageKey), RelatedMessage.class).toString();
        System.out.println("From redis --> Related message key : " + relatedMessageKey + " and value : " + value);
        System.out.println("------------------------------------");
        return relatedMessage;
    }
}
