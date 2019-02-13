package com.fengquanwei.muse.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Curator 用法
 *
 * @author fengquanwei
 * @create 2019/1/31 10:03
 **/
public class CuratorUsage {
    private static Logger logger = LoggerFactory.getLogger(CuratorUsage.class);

    /**
     * Curator 用法
     */
    public static void main(String[] args) {
        logger.info("========== 创建会话 ==========");

        // 构造客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/test")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        // 启动客户端
        client.start();

        logger.info("connect zookeeper success");

        sleep(1000);
    }

    /**
     * 休眠一会
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error("sleep error", e);
        }
    }
}
