package greeter;


import org.apache.flink.statefun.examples.greeter.generated.GreetRequest;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;


public class test {
    public static void main(String[] args) {
//        JedisPool redisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 5000, false);
//        Jedis jedis = redisPool.getResource();
//        jedis.xgroupCreate("serving_stream", "serving", new StreamEntryID(0, 0), true);
//        System.out.println("创建成功");
//        while (true) {
//            long start = System.nanoTime();
//            String groupName = "serving";
//            String consumerName = "consumer-" + UUID.randomUUID().toString();
//            List<Map.Entry<String, List<StreamEntry>>> response = jedis.xreadGroup(
//                    groupName,
//                    consumerName,
//                    1,
//                    1,
//                    false,
//                    new AbstractMap.SimpleEntry("serving_stream", StreamEntryID.UNRECEIVED_ENTRY));
//
//            if (response != null) {
//                for (int i = 0; i < response.size(); i += 1) {
//                    Map.Entry<String, List<StreamEntry>> streamMessages = response.get(i);
//                    String key = streamMessages.getKey();
//                    List<StreamEntry> entries = streamMessages.getValue();
//                    String name = entries.get(0).getFields().get("name");
//                    GreetRequest guess = GreetRequest.newBuilder().setWho(name).build();
//                    System.out.println(guess);
//                }
//            }
//        }


        JedisPool redisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, false);
        Jedis jedis = redisPool.getResource();
        Pipeline ppl = jedis.pipelined();
        Map<String, String> content = new HashMap<>();
        content.put("cwpeng", "hello cwpeng");
        ppl.hmset("result", content);
        ppl.sync();
        jedis.close();
        System.out.println("键入成功");
    }
}

