package com.fengquanwei.muse.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public static void main(String[] args) throws Exception {
        logger.info("========== 创建会话 ==========");

        // 构造客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curator")
                .build();

        // 启动客户端
        client.start();

        logger.info("client start");

        logger.info("========== 清理环境 ==========");

        // 删除根节点及其所有子节点
        client.delete().deletingChildrenIfNeeded().forPath("");

        // 创建根节点
        client.create().forPath("");

        logger.info("clean");

        logger.info("========== 创建节点 ==========");

        String path1 = "/a";
        String path2 = "/b";
        String path3 = "/c";
        String path4 = "/d/dd/ddd";
        String path5 = "/e";

        // 创建持久节点，初始内容为空
        client.create().forPath(path1);
        logger.info("create node, path: {}", path1);

        // 创建持久节点，附带初始内容
        client.create().forPath(path2, "B".getBytes());
        logger.info("create node, path: {}", path2);

        // 创建临时节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path3, "C".getBytes());
        logger.info("create node, path: {}", path3);

        // 递归创建父节点
        client.create().creatingParentsIfNeeded().forPath(path4, "D".getBytes());
        logger.info("create node, path: {}", path4);

        client.create().forPath(path5);
        logger.info("create node, path: {}", path5);

        logger.info("========== 读取数据 ==========");

        // 读取节点数据
        byte[] data2 = client.getData().forPath(path2);
        logger.info("get data, path: {}, data: {}", path2, new String(data2));

        // 读取节点数据同时获取 stat
        Stat stat3 = new Stat();
        byte[] data3 = client.getData().storingStatIn(stat3).forPath(path3);
        logger.info("get data, path: {}, data: {}, stat: {}", path3, new String(data3), stat3);

        logger.info("========== 更新数据 ==========");

        // 更新数据
        Stat stat22 = client.setData().forPath(path2, "BB".getBytes());
        logger.info("set data, path: {}, data: {}, stat: {}", path2, new String(client.getData().forPath(path2)), stat22);

        // 指定版本更新
        Stat stat222 = client.setData().withVersion(stat22.getVersion()).forPath(path2, "BBB".getBytes());
        logger.info("set data, path: {}, data: {}, stat: {}", path2, new String(client.getData().forPath(path2)), stat222);

        logger.info("========== 删除节点 ==========");

        // 删除叶子节点
        client.delete().forPath(path1);
        logger.info("delete node, path: {}", path1);

        // 指定版本删除节点
        client.delete().withVersion(-1).forPath(path2);
        logger.info("delete node, path: {}", path2);

        // 删除节点并递归删除其所有叶子节点
        String parentPath = path4.substring(0, path4.indexOf("/", 1));
        client.delete().deletingChildrenIfNeeded().forPath(parentPath);
        logger.info("delete node, path: {}", parentPath);

        // 强制删除节点
        client.delete().guaranteed().forPath(path5);
        logger.info("delete node, path: {}", path5);

        logger.info("========== 异步接口 ==========");

        // 异步创建节点，使用默认线程处理异步通知
        client.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                logger.info("【{}】process result, curatorEvent: {}", Thread.currentThread().getName(), curatorEvent);
            }
        }).forPath("/f", "F".getBytes());

        // 异步创建节点，使用自定义线程池处理异步通知
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        client.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                logger.info("【{}】process result, curatorEvent: {}", Thread.currentThread().getName(), curatorEvent);
            }
        }, executorService).forPath("/f", "F".getBytes());

        Thread.sleep(3000);

        executorService.shutdown();
    }
}
