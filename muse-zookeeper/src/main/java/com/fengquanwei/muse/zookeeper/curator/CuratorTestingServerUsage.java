package com.fengquanwei.muse.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingServer;
import org.apache.curator.test.TestingZooKeeperServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Curator TestingServer 用法
 *
 * @author fengquanwei
 * @create 2019/3/5 11:23
 **/
public class CuratorTestingServerUsage {
    private static final Logger logger = LoggerFactory.getLogger(CuratorTestingServerUsage.class);

    /**
     * Curator TestingServer 用法
     */
    public static void main(String[] args) throws Exception {
        logger.info("========== TestingServer ==========");

        testingServer();

        logger.info("========== testingCluster ==========");

        testingCluster();
    }

    /**
     * 本地 zk 服务器
     */
    private static void testingServer() throws Exception {
        String path = "/zookeeper";
        TestingServer server = new TestingServer(2184, new File("datas/zk-data"));

        // 构造客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(server.getConnectString())
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        // 启动客户端
        client.start();

        List<String> list = client.getChildren().forPath(path);

        logger.info("path: {}, children: {}", path, list);

        Thread.sleep(1000);

        server.close();
    }

    /**
     * 本地 zk 集群
     */
    private static void testingCluster() throws Exception {
        TestingCluster cluster = new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);

        TestingZooKeeperServer leader = null;

        for (TestingZooKeeperServer server : cluster.getServers()) {
            InstanceSpec instanceSpec = server.getInstanceSpec();

            int serverId = instanceSpec.getServerId();
            String connectString = instanceSpec.getConnectString();
            String serverState = server.getQuorumPeer().getServerState();
            String dataDirectory = instanceSpec.getDataDirectory().getAbsolutePath();

            logger.info("serverId: {}, connectString: {}, serverState: {}, dataDirectory: {}", serverId, connectString, serverState, dataDirectory);

            if (serverState.equals("leading")) {
                leader = server;
            }
        }
        leader.kill();

        Thread.sleep(1000);

        logger.info("leader killed");

        for (TestingZooKeeperServer server : cluster.getServers()) {
            int serverId = server.getInstanceSpec().getServerId();
            String serverState = server.getQuorumPeer().getServerState();
            String dataDirectory = server.getInstanceSpec().getDataDirectory().getAbsolutePath();

            logger.info("serverId: {}, serverState: {}, dataDirectory: {}", serverId, serverState, dataDirectory);
        }

        Thread.sleep(1000);

        cluster.stop();
    }
}
