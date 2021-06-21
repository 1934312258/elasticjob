package com.kevin.esjob.job;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/5/17 18:12
 */
@Component
public class Second implements DataflowJob {
    int times = 0;
    int value = 0;
    //  此方法获取数据，如果list不为空会继续向下调用，调用到该实例的processData方法，否则会直接返回
    //流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去；
    // 非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
    //如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止。
    // 流式数据处理参照TbSchedule设计，适用于不间歇的数据处理。
    // 流式处理的配置 elasticjob.jobs.Second.props=streaming.process=true
    @Override
    public List fetchData(ShardingContext shardingContext) {

        System.out.println("进程执行了"+times+++"==="+"分片"+shardingContext.getShardingItem()+"="+ System.currentTimeMillis());
        System.out.println("任务id："+ shardingContext.getTaskId());
        return Arrays.asList("a");
    }

    @Override
    public void processData(ShardingContext shardingContext, List list) {
        System.out.println("data"+times+"==="+"分片"+shardingContext.getShardingItem()+"="+ System.currentTimeMillis());
    }
}
