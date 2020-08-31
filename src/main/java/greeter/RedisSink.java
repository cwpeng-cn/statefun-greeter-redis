package greeter;

import org.apache.flink.statefun.examples.greeter.generated.GreetResponse;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.Map;

public class RedisSink implements SinkFunction<GreetResponse> {
    JedisPool redisPool = null;
    Jedis jedis = null;

    RedisSink() {
        System.out.println("RedisSink");
        redisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, false);
        jedis = redisPool.getResource();
    }

    @Override
    public void invoke(GreetResponse value, Context context) throws Exception {
        Pipeline ppl = jedis.pipelined();
        Map<String, String> content = new HashMap<>();
        content.put(value.getWho(), value.getGreeting());
        ppl.hmset("result", content);
        ppl.sync();
        jedis.close();
        System.out.println(content);
    }
}
