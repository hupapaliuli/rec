package rechistory.serializer;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

//序列化器1，采用java字节流进行序列化，可以再扩展pb序列化等等方式
@Slf4j
public class JavaSerializer implements Serializer {


    @Override
    public byte[] serialize(BloomFilter<String> bloomFilter) {
        //String 可以直接getByte获取得到，一个java对象如何获取到byte？ 需要使用 ObjectOutputStream  和 ByteArrayOutputStream
        /*
        Object object = new Object();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            redis.set("key".getBytes(),bytes)
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        //判空是一个编程的好习惯
        if(bloomFilter == null){
            //throw new RuntimeException("bloomFilter is null");
            return null;
        }
        //布隆过滤器本身提供了一个写出字节流的方法，写到ByteArrayOutputStream中即可。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bloomFilter.writeTo(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            //如何对待这种错误？ 有多种解决办法，可以直接往外抛出，也可以直接吞噬异常，然后返回一个null给到客户端，是否抛出，得看客户端是不是处理得了异常
            log.info("serialize error : ",e);
        }
        return null;
    }

    @Override
    public BloomFilter<String> deserialize(byte[] bytes) {
        if(bytes == null){
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        BloomFilter<String> bloomFilter = null;
        try {
            //布隆过滤器提供了从字节数组中反序列化为布隆过滤器的方法
            bloomFilter = BloomFilter.readFrom(byteArrayInputStream,Funnels.stringFunnel(Charset.forName("utf-8")));
        } catch (IOException e) {
            log.error("deserialize error: ",e);
        }
        return bloomFilter;
    }
}
