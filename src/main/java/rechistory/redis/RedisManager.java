package rechistory.redis;

public interface RedisManager {

    void set(String key,String value);


    void set(String key,byte[] value);

    void setEx(String key,byte[] value);


    byte[] get(String key);



}
