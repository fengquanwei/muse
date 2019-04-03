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

        logger.info("创建会话成功");

        logger.info("========== ZKPaths ==========");

        String namespace = "/tools";

        String path1 = ZKPaths.fixForNamespace(namespace, "sub");
        logger.info("fixForNamespace: {}", path1);

        String path2 = ZKPaths.makePath(namespace, "sub");
        logger.info("makePath: {}", path2);

        String node1 = ZKPaths.getNodeFromPath(namespace + "/aa/aaa");
        logger.info("getNodeFromPath: {}", node1);

        ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode(namespace + "/aa/aaa");
        String path3 = pathAndNode.getPath();
        String node3 = pathAndNode.getNode();
        logger.info("pathAndNode, path: {}, node: {}", path3, node3);

        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();

        String path4 = namespace + "/child1";
        String path5 = namespace + "/child2";
        ZKPaths.mkdirs(zooKeeper, path4);
        ZKPaths.mkdirs(zooKeeper, path5);
        List<String> sortedChildren = ZKPaths.getSortedChildren(zooKeeper, namespace);
        logger.info("sortedChildren: {}", sortedChildren);

        ZKPaths.deleteChildren(zooKeeper, namespace, false);

        logger.info("========== EnsurePaths ==========");

        String path6 = ZKPaths.fixForNamespace(namespace, "/p");
        EnsurePath ensurePath1 = new EnsurePath(path6);
        ensurePath1.ensure(client.getZookeeperClient());
        logger.info("静默创建节点, 节点路径: {}", path6);

        String path7 = ZKPaths.fixForNamespace(namespace, "/q");
        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath(path7);
        ensurePath2.ensure(client.getZookeeperClient());
        logger.info("静默创建节点, 节点路径: {}", path7);
    }
}
