package com.fengquanwei.muse.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Curator 工具类用法
 *
 * @author fengquanwei
 * @create 2019/3/5 10:21
 **/
public class CuratorToolsUsage {
    private static final Logger logger = LoggerFactory.getLogger(CuratorToolsUsage.class);

    /**
     * 典型使用场景
     */
    public static void main(String[] args) throws Exception {
        logger.info("========== 创建会话 ==========");

        // 构造客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        // 启动客户端
        client.start();

        logger.info("client start");

        logger.info("========== ZKPaths ==========");

        String namespace = "/tools";
        String p1 = ZKPaths.fixForNamespace(namespace, "sub");
        String p2 = ZKPaths.makePath(namespace, "sub");
        String n1 = ZKPaths.getNodeFromPath(namespace + "/aa/aaa");

        ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode(namespace + "/aa/aaa");
        String p = pathAndNode.getPath();
        String n = pathAndNode.getNode();

        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();

        String path1 = namespace + "/child1";
        String path2 = namespace + "/child2";
        ZKPaths.mkdirs(zooKeeper, path1);
        ZKPaths.mkdirs(zooKeeper, path2);
        List<String> sortedChildren = ZKPaths.getSortedChildren(zooKeeper, namespace);

        ZKPaths.deleteChildren(zooKeeper, namespace, true);

        logger.info("========== EnsurePaths ==========");

        client.usingNamespace("tools");

        EnsurePath ensurePath1 = new EnsurePath("/p");
        ensurePath1.ensure(client.getZookeeperClient());

        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/q");
        ensurePath2.ensure(client.getZookeeperClient());
    }
}
