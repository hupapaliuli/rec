package rechistory.core;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import rechistory.bean.BloomFilteHolder;
import rechistory.bean.Configuration;
import rechistory.redis.RedisManager;
import rechistory.serializer.Serializer;

import java.nio.charset.Charset;

public abstract class AbstractExposureHistoryManager implements ExposureHistoryManager{


    Configuration configuration;

    RedisManager redisManager;

    //是Manager持有ExporsureHistory对象，还是倒过来？  ExporsureHistory对象反过来持有一个Manger对象。  这又是什么一种模式？？？
    //序列化器？？？
    Serializer serializer;

    public AbstractExposureHistoryManager(Configuration configuration, RedisManager redisManager,Serializer serializer) {
        this.configuration = configuration;
        this.redisManager = redisManager;
        this.serializer = serializer;
    }

    @Override
    public ExposureHistory getExposureHistory(String uid){
        //构建一个空的ConcreteExposureHistory ？  还是从redis中获取一个已经有数据的ConcreteExposureHistory   还是分成两个方法？
        //是个好问题，如果一个反而，可以考虑抽象两个方法出来的嘛，
        //曝光历史，因为只支持全量覆盖，所以考虑每次写入就是全量写入，那么获取曝光历史，就是
        String redisKey = String.format(configuration.redisKey,uid);
        byte[] bytes = redisManager.get(redisKey);
        BloomFilter<String> bloomFilter = serializer.deserialize(bytes);
        if(bloomFilter == null){
            bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),configuration.getMaxHistorySize(),configuration.getFpp());
        }
        BloomFilteHolder bloomFilteHolder = BloomFilteHolder.builder().redisKey(redisKey).bloomFilter(bloomFilter).build();
        return new BloomFilterExposureHistory(bloomFilteHolder ,this) ;
    }

    @Override
    public ExposureHistory newExposureHistory(String uid){
        //这种小转换，直接在里面写就好了，不用就纠结封装什么的
        String redisKey = String.format(configuration.redisKey,uid);
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),configuration.getMaxHistorySize(),configuration.getFpp());
        BloomFilteHolder bloomFilteHolder = BloomFilteHolder.builder().redisKey(redisKey).bloomFilter(bloomFilter).build();
        return new BloomFilterExposureHistory(bloomFilteHolder ,this) ;
    }

    public void save(BloomFilteHolder bloomFilteHolder){
        String redisKey = bloomFilteHolder.getRedisKey();
        BloomFilter<String> bloomFilter = bloomFilteHolder.getBloomFilter();
        redisManager.setEx(redisKey, serializer.serialize(bloomFilter));
    }

}
