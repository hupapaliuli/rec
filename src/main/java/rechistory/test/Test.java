package rechistory.test;

import rechistory.bean.Configuration;
import rechistory.core.CommonExposureHistoryManager;
import rechistory.core.ExposureHistory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {


        Jedis jedis  = new Jedis("redis://localhost:6379");
        String testStr = jedis.set("2","222");

        System.out.println(testStr);

        //一个需求，要把曝光历史，由一堆一堆的newsId组成（不必含有timestamp属性），做成布隆过滤器，并且存储到redis中，如何做到？
        List<String> list = new ArrayList<String>();
        list.add("newsId123456");
        list.add("newsId234567");
        list.add("newsId345678");

        String userId = "hjf";

        //思考1 ： 客户端需要拿到一个BloomFilterExposureHistory 进行存储操作 ，是通过Manager去拿呢？  还是直接new一个BloomFilterExposureHistory出来呢？？？？
        //一般来说，通过一个Manager来创建会更容易控制。
        Configuration configuration = Configuration.builder().redisKey("exp:his:{%s}")
                                        .redisAddr("redis://localhost:6379")
                                        .expireTime(10000)
                                        .maxHistorySize(100000)
                                        .fpp(0.001)
                                        .build();

        CommonExposureHistoryManager commonExposureHistoryManager = new CommonExposureHistoryManager(configuration);
        ExposureHistory exposureHistory = commonExposureHistoryManager.newExposureHistory(userId);
        exposureHistory.putElements(list);
        exposureHistory.sava();

        ExposureHistory exposureHistory1 =  commonExposureHistoryManager.getExposureHistory(userId);

        System.out.println(exposureHistory1.contains("11111"));
        System.out.println(exposureHistory1.contains("newsId123456"));
        System.out.println(exposureHistory1.contains("newsId234567"));
        System.out.println(exposureHistory1.contains("newsId345678"));
        System.out.println(exposureHistory1.contains("newsId34567899"));




    }

}
