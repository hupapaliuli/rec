package rechistory.core;

import rechistory.bean.BloomFilteHolder;

import java.util.List;



//原本ConcreteExposureHistory，不够见名知意，改了一版名字
public class BloomFilterExposureHistory implements ExposureHistory {


    BloomFilteHolder bloomFilteHolder;



    //是ExporsureHistoryManager持有ExporsureHistory对象，还是倒过来？  ExporsureHistory对象反过来持有一个ExporsureHistoryManager对象。 需要思考清楚，这又是一种什么模式？、？
    //其实最终的目的都应该是看客户端，最终使用你这个工具的客户端，需要知道的信息越少越好
    //答案：肯定是ExporsureHistory对象反过来持有一个ExporsureHistoryManager对象才是合理的，因为每个ExporsureHistory其实都需要一个管理者，来进行持久层交互，
    // 而且从语义上来讲的话，ExporsureHistoryManager持有一个ExporsureHistory对象，也是不合理的，因为ExporsureHistory对象是和每个用户uid绑定的，肯定不适合被
    //ExporsureHistoryManager进行持有

    //是中介者模式？  显然不是  中介者模式指的是多个对象之间有复杂的交互关系， 通过中介，对象都和中介打交道而不再需要知道各类的对象。而中介需要知道所有的对象，
    //需要持有所有的具体对象，然后通过中介来做到多个对象直接的交互,常见的一个问题是，中介类因为要维护很多对象之间的相互关系，所以中介会越来越臃肿。
    //但是类比一下的话，其实买房和卖房的客户，持有一个中介对象，以及中介持有一堆的客户，其实这也是合理的，所以ExporsureHistory持有一个ExporsureHistoryManager其实从实际生活含义中来讲，其实是可以接收的。

    AbstractExposureHistoryManager exporsureHistoryManager;

    public BloomFilterExposureHistory(BloomFilteHolder bloomFilteHolder,AbstractExposureHistoryManager exporsureHistoryManager ){
        this.bloomFilteHolder = bloomFilteHolder;
        this.exporsureHistoryManager = exporsureHistoryManager;
    }


    //思考：  每个BloomFilterExposureHistory和redis交互其实不太合理，是否考虑交给到ExporsureHistoryManager进行交互会更加合理一些？
    public void sava() {
        exporsureHistoryManager.save(bloomFilteHolder);
    }

    @Override
    public boolean contains(String newsId) {
        return this.bloomFilteHolder.getBloomFilter().mightContain(newsId);
    }


    public void putElement(String newsId) {
        this.bloomFilteHolder.getBloomFilter().put(newsId);
    }

    @Override
    public void putElements(List<String> newsIds) {
        for(String newsId  : newsIds){
            this.bloomFilteHolder.getBloomFilter().put(newsId);
        }
    }


}
