package rechistory.bean;


import com.google.common.hash.BloomFilter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloomFilteHolder {

    String redisKey;

    BloomFilter<String> bloomFilter;

}
