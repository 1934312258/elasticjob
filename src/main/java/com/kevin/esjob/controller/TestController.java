package com.kevin.esjob.controller;

import com.kevin.esjob.job.FirstElasticJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.http.props.HttpJobProperties;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.OneOffJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/6/11 15:21
 */
@RestController
@Slf4j
@RequestMapping("/job")
public class TestController {

    @Autowired
    CoordinatorRegistryCenter coordinatorRegistryCenter;

    @RequestMapping("/test")
    public void test(String source, @RequestHeader String shardingContext){
        log.info("execute from source : {}, shardingContext : {}", source, shardingContext);
        System.out.println("http job 执行了");
    }

    @RequestMapping("/add")
    public void add(){
//        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration("127.0.0.1:2181", "elastic-job-demo"));
//        regCenter.init();

        JobConfiguration jobConfiguration = JobConfiguration.newBuilder("javaJob", 1)
                .setProperty(HttpJobProperties.URI_KEY, "http://localhost:6666/job/test")
                .setProperty(HttpJobProperties.METHOD_KEY, "GET")
                .setProperty(HttpJobProperties.DATA_KEY, "source=ejob")
                // 设置是否流式处理
                .setProperty("streaming.process","true")
                .cron("0/5 * * * * ?")
                // 个性化分片参数，0代表分片item，多个使用逗号隔开
                .shardingItemParameters("0=Beijing,1=TianJin")
                //job自定义参数
                .jobParameter("kevin")
                // 是否开启失效转移,该配置针对进程挂掉，且job执行前的重新分片已经执行完成或者任务执行过程中分片挂掉，
                // 其他进程执行down机进程的分片任务
                .failover(true)
                // 是否开启错过任务重新执行
                .misfire(true)
                // 允许本机与注册中心最大的时间误差，默认-1，即不检查
                .maxTimeDiffSeconds(-1)
                // 修复作业服务器不一致状态服务调度间隔分钟 ,默认10分钟
                .reconcileIntervalMinutes(10)
                // 设置作业分片策略 默认AVG_ALLOCATION
                .jobShardingStrategyType("AVG_ALLOCATION")
                // 作业线程池处理策略 默认CPU
                .jobExecutorServiceHandlerType("CPU")
                // 作业错误处理策略 默认LOG
                .jobErrorHandlerType("LOG")
                // 作业描述信息
                .description("作业描述信息")
                // 本地配置是否可覆盖注册中心配置，设置为true后新启动的job JobConfiguration如果有变动，则已启动的job进程也会使用新的配置
                // 此处需要注意如果overwrite设置为false且配置中心已经有该job的配置，那么即使在项目中更改配置，启动后也不会生效
                .overwrite(true)
                // 监控任务的执行状态，如果任务执行的时间较短不建议开启以提升性能
                .monitorExecution(false)
                // 作业是否禁止启动
                .disabled(false)
                .jobListenerTypes("simpleJob")
                .build();
        new ScheduleJobBootstrap(coordinatorRegistryCenter, new FirstElasticJob(), jobConfiguration).schedule();
//        new ScheduleJobBootstrap(coordinatorRegistryCenter, new FirstElasticJob(), jobConfiguration).schedule();
    }

    @RequestMapping("/addOne")
    public void addOne(){
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder("myHttpJob", 1)
                .setProperty(HttpJobProperties.URI_KEY, "http://localhost:6666/test")
                .setProperty(HttpJobProperties.METHOD_KEY, "Get")
                .setProperty(HttpJobProperties.DATA_KEY, "source=ejob")
                // 设置是否流式处理
                .setProperty("streaming.process","true")
//                .cron("0/5 * * * * ?")
                .build();
        OneOffJobBootstrap oneOffJobBootstrap = new OneOffJobBootstrap(coordinatorRegistryCenter,new FirstElasticJob(),jobConfiguration);
        oneOffJobBootstrap.execute();
    }
}
