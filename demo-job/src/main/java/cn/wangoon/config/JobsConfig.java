package cn.wangoon.config;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.utils.LogUtils;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 将zk  cron  job 建立关系
 * @Remark
 * @PackagePath cn.wangoon.common.config.JobsConfig
 * @Author YINZHIYU
 * @Date 2019-10-15 10:15:00
 * @Version 1.0.0.0
 **/
@Configuration
public class JobsConfig {

    @Resource
    public ZookeeperRegistryCenter regCenter;

    public JobsConfig() {
    }

    /**
     * @Description job zk cron 建立关系
     * @Remark
     * @Params ==>
     * @Param elasticJob
     * @Param cron
     * @Param shardingTotalCount
     * @Param shardingItemParameters
     * @Return com.dangdang.ddframe.job.lite.api.JobScheduler
     * @Date 2019-10-15 10:15:00
     * @Version 1.0.0.0
     */
    public JobScheduler addJobScheduler(final ElasticJob elasticJob, final String cron, final int shardingTotalCount, final String shardingItemParameters) {

        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(elasticJob, cron, shardingTotalCount, shardingItemParameters);

        if (ObjectUtil.isEmpty(liteJobConfiguration)) {
            return null;
        }

        return new SpringJobScheduler(elasticJob, regCenter, liteJobConfiguration);
    }

    /**
     * @Description 表达式
     * @Remark
     * @Params ==>
     * @Param elasticJob
     * @Param cron
     * @Param shardingTotalCount
     * @Param shardingItemParameters
     * @Return com.dangdang.ddframe.job.lite.config.LiteJobConfiguration
     * @Date 2019-10-15 10:15:00
     * @Version 1.0.0.0
     */
    public LiteJobConfiguration getLiteJobConfiguration(final ElasticJob elasticJob, final String cron, final int shardingTotalCount, final String shardingItemParameters) {

        JobTypeConfiguration jobTypeConfiguration = null;

        //意为简单实现，未经任何封装的类型。需实现SimpleJob接口。该接口仅提供单一方法用于覆盖，此方法将定时执行。与Quartz原生接口相似，但提供了弹性扩缩容和分片等功能。
        if (elasticJob instanceof SimpleJob) {
            jobTypeConfiguration = new SimpleJobConfiguration(
                    JobCoreConfiguration.newBuilder(
                            elasticJob.getClass().getName(),
                            cron,
                            shardingTotalCount
                    ).shardingItemParameters(shardingItemParameters).build(),
                    elasticJob.getClass().getCanonicalName()
            );
        }

        /*
         * Dataflow类型用于处理数据流，需实现DataflowJob接口。
         * 可通过DataflowJobConfiguration配置是否流式处理。
         *  1.流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去；
         *  2.非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
         *  如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止。
         *  流式数据处理参照TbSchedule设计，适用于不间歇的数据处理。
         *
         * streamingProcess: 是否流式处理数据
         * oms统一采用非流式处理数据规则
         */
        if (elasticJob instanceof DataflowJob) {
            jobTypeConfiguration = new DataflowJobConfiguration(
                    JobCoreConfiguration.newBuilder(
                            elasticJob.getClass().getName(),
                            cron,
                            shardingTotalCount
                    ).shardingItemParameters(shardingItemParameters).build(),
                    elasticJob.getClass().getCanonicalName(),
                    false
            );
        }
        if (ObjectUtil.isEmpty(jobTypeConfiguration)) {
            return null;
        }
        return LiteJobConfiguration.newBuilder(jobTypeConfiguration).overwrite(true).build();
    }

    /**
     * @Description 获取某个节点下的所有子节点
     * @Remark
     * @Params ==>
     * @Param nodePath
     * @Return java.util.List<java.lang.String>
     * @Date 2020/9/16 16:09
     * @Auther YINZHIYU
     */
    public List<String> getChildNodes(String nodePath) {
        List<String> childrenList = Lists.newArrayList();
        try {
            CuratorFramework client = regCenter.getClient();
            if (ObjectUtil.isEmpty(client)) {
                return childrenList;
            }
            Stat stat = new Stat();
            GetChildrenBuilder childrenBuilder = client.getChildren();
            childrenList.addAll(childrenBuilder.storingStatIn(stat).forPath(nodePath));
        } catch (Exception e) {
            LogUtils.error(String.format("获取Zookeeper下Oms系统 节点%s下所有节点 ==> 异常", nodePath), e);
        }

        return childrenList.stream().map(item -> item = String.format("/%s", item)).collect(Collectors.toList());
    }

    /**
     * @Description 删除某个节点及子节点
     * @Remark
     * @Params ==>
     * @Param nodePath
     * @Return void
     * @Date 2020/9/16 17:04
     * @Auther YINZHIYU
     */
    public void deleteNode(String nodePath) {
        try {
            CuratorFramework client = regCenter.getClient();
            if (ObjectUtil.isEmpty(client)) {
                return;
            }
            Stat stat = new Stat();
            client.getData().storingStatIn(stat).forPath(nodePath);
            client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(nodePath);
        } catch (Exception e) {
            LogUtils.error(String.format("删除Zookeeper下Oms系统 节点%s以及子节点 ==> 异常", nodePath), e);
        }
    }
}