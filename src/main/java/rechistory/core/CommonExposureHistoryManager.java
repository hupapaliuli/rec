package rechistory.core;

import rechistory.bean.Configuration;
import rechistory.redis.JedisRedisManager;
import rechistory.serializer.JavaSerializer;

public class CommonExposureHistoryManager extends AbstractExposureHistoryManager {
    public CommonExposureHistoryManager(Configuration configuration) {
        super(configuration, new JedisRedisManager(configuration),  new JavaSerializer());
    }
}
