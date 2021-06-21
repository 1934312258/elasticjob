package com.kevin.esjob.listener;

import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.apache.shardingsphere.elasticjob.lite.api.listener.AbstractDistributeOnceElasticJobListener;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/6/17 11:17
 */
public class MyDistributeOnceJobListener extends AbstractDistributeOnceElasticJobListener{
    static long startedTimeoutMilliseconds = 6000L;
    static long completedTimeoutMilliseconds = 6000L;
    public MyDistributeOnceJobListener() {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
        System.out.println("pre最後執行的分片：=================================="+shardingContexts.getJobName());
    }

    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
        System.out.println("aft最後執行的分片2：=================================="+shardingContexts.getJobName());
    }

    @Override
    public String getType() {
        return "distributeOnceJobListener";
    }
}
