package rechistory.redis;

import redis.clients.jedis.JedisCluster;

public class JedisClusterManager implements RedisManager{

    JedisCluster jedisCluster;

    @Override
    public void set(String key, String value) {

    }

    @Override
    public void set(String key, byte[] value) {

    }

    @Override
    public void setEx(String key, byte[] value) {

    }

    @Override
    public byte[] get(String key) {
        return new byte[0];
    }
}
