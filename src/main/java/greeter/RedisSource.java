package greeter;

import org.apache.flink.statefun.examples.greeter.generated.GreetRequest;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import redis.clients.jedis.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RedisSource implements SourceFunction<GreetRequest> {

    JedisPool redisPool = null;
    Jedis jedis = null;
    Boolean isRunning = true;


    public RedisSource() {
        System.out.println("RedisSource");
        redisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 5000, false);
        jedis = this.redisPool.getResource();
        jedis.xgroupCreate("serving_stream", "serving", new StreamEntryID(0, 0), true);
    }

    @Override
    public void run(SourceContext<GreetRequest> sourceContext) throws Exception {
        while (isRunning) {
            String groupName = "serving";
            String consumerName = "consumer-" + UUID.randomUUID().toString();
            List<Map.Entry<String, List<StreamEntry>>> response = jedis.xreadGroup(
                    groupName,
                    consumerName,
                    1,
                    1,
                    false,
                    new AbstractMap.SimpleEntry("serving_stream", StreamEntryID.UNRECEIVED_ENTRY));

            if (response != null) {
                for (int i = 0; i < response.size(); i += 1) {
                    Map.Entry<String, List<StreamEntry>> streamMessages = response.get(i);
                    List<StreamEntry> entries = streamMessages.getValue();
                    String name = entries.get(0).getFields().get("name");
                    GreetRequest guess = GreetRequest.newBuilder().setWho(name).build();
                    sourceContext.collect(guess);
                    System.out.println(guess);
                }
            }
        }
    }


    @Override
    public void cancel() {
        isRunning = false;
        jedis.close();
        redisPool.close();
    }
}
