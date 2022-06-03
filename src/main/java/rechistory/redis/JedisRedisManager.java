package rechistory.redis;

import lombok.extern.slf4j.Slf4j;
import rechistory.bean.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;

@Slf4j
public class JedisRedisManager implements RedisManager {


    JedisPool jedisPool;
    int expireTime;

    //
    Configuration configuration;

    public JedisRedisManager(Configuration configuration){
        //简单处理jedispool，如果需要对超时时间设置，可以通过JedisPoolConfig进行设置，但是谨记不要只用一个全局的Jedis，这样会有客户端连接的瓶颈
        jedisPool = new JedisPool(configuration.getRedisAddr());
        this.expireTime = configuration.getExpireTime();
    }


    @Override
    public void set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key,value);
    }

    @Override
    public void set(String key, byte[] value) {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.set(key.getBytes("utf-8"),value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("xxx");
        }
    }

    @Override
    public void setEx(String key, byte[] value) {
        try {
            Jedis jedis = jedisPool.getResource();
            System.out.println(jedis);
            jedis.setex(key.getBytes("utf-8"),expireTime,value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("xxx");
        }
    }

    @Override
    public byte[] get(String key) {
        byte[] bytes = null;
        try {
            Jedis jedis = jedisPool.getResource();
            System.out.println(jedis);
            bytes = jedis.get(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("redis get error: ",e);
            return null;
        }
        return bytes;
    }
}
