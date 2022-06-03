package rechistory.core;

import java.util.List;


/**
 *
 */
//抽象的没啥问题，大致也就是这么几个方法，  本地缓存记录、  存储到持久层  判断是否还含有
public interface History {

    /**
     * 存储到持久层
     */
    void sava();

    boolean contains(String newsId);

    /**
     * 记录到程序缓存的BloomFilter中
     * @param newsId
     */
    void putElement(String newsId);

    /**
     * 批量记录到Bloomfilter中
     * @param newsIds
     */
    void putElements(List<String> newsIds);

}
