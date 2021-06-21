package com.kevin.esjob.job;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/5/17 17:23
 */
@Component
public class FirstElasticJob implements SimpleJob {
    int times = 0;
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(System.currentTimeMillis()+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.out.println(
                shardingContext.getShardingTotalCount()+"=========="+
                shardingContext.getShardingItem()+"========="+shardingContext.getShardingParameter()+"========="+shardingContext.getJobParameter());
        //        System.out.println(System.currentTimeMillis());
//        System.out.println(1/0);
//        System.out.println("进程执行了"+times+++"==="+"分片"+shardingContext.getShardingItem()+"="+ System.currentTimeMillis());
    }
}
