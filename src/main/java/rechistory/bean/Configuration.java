package rechistory.bean;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Configuration {

    public String redisKey = "exp:his:{%s}";

    public String redisAddr;

    public Integer expireTime ;

    public int redisTimeOut;
    /**
     * 布隆过滤器存储最大值
     */
    public Integer maxHistorySize = 100000;

    /**
     * 布隆过滤器误判率 默认值给0.001
     */
    public double fpp = 0.001;

}
