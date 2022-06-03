package rechistory.serializer;

import com.google.common.hash.BloomFilter;

public interface Serializer {


    //序列化器，需要考虑需要实现哪些的功能，有哪些行为，超类需要有哪些属性


    byte[] serialize(BloomFilter<String> bloomFilter);


    BloomFilter<String> deserialize(byte[] bytes);

}
