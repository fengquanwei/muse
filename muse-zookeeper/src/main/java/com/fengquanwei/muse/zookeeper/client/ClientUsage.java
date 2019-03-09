package com.fengquanwei.muse.zookeeper.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 客户端用法
 *
 * @author fengquanwei
 * @create 2019/1/31 10:02
 **/
public class ClientUsage {
    private static final Logger logger = LoggerFactory.getLogger(ClientUsage.class);

    // zk 客户端
    private static ZooKeeper zooKeeper;

    /**
     * 客户端用法
     */
    public static void main(String[] args) throws Exception {
        logger.info("========== 创建会话 ==========");
        createSession();
        Thread.sleep(1000);

//        long sessionId = zooKeeper.getSessionId();
//        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
//
//        logger.info("========== 复用会话 ==========");
//        reuseSession(1, "wrongPasswd".getBytes());
//        Thread.sleep(1000);
//
//        logger.info("========== 复用会话 ==========");
//        reuseSession(sessionId, sessionPasswd);
//        Thread.sleep(1000);

        logger.info("========== 清理环境 ==========");
        clean();
        Thread.sleep(1000);

        logger.info("========== 创建节点 ==========");
        createNode();
        Thread.sleep(1000);

        logger.info("========== 获取子节点列表 ==========");
        getChildrenNode();
        Thread.sleep(1000);

        logger.info("========== 读取数据 ==========");
        getData();
        Thread.sleep(1000);

        logger.info("========== 更新数据 ==========");
        setData();
        Thread.sleep(1000);

        logger.info("========== 检测节点是否存在 ==========");
        exists();
        Thread.sleep(1000);

        logger.info("========== 删除节点 ==========");
        deleteNode();
        Thread.sleep(1000);

        logger.info("========== 权限控制 ==========");
        auth();
        Thread.sleep(1000);
    }

    /**
     * 创建会话
     */
    private static void createSession() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 创建 zk 实例
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/client", 5000, new MyWatcher(countDownLatch));

        // 等待 zk 连接
        boolean reached = countDownLatch.await(6000, TimeUnit.MILLISECONDS);

        // 打印连接状态
        if (reached) {
            logger.info("connect zookeeper success");
        } else {
            logger.error("connect zookeeper timeout");
        }
    }

    /**
     * 复用会话
     */
    private static void reuseSession(long sessionId, byte[] sessionPasswd) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 创建 zk 实例
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/client", 5000, new MyWatcher(countDownLatch), sessionId, sessionPasswd);

        // 等待 zk 连接
        boolean reached = countDownLatch.await(6000, TimeUnit.MILLISECONDS);

        // 打印连接状态
        if (reached) {
            logger.info("connect zookeeper success");
        } else {
            logger.error("connect zookeeper timeout");
        }
    }

    /**
     * 清理环境
     */
    private static void clean() throws Exception {
        String root = "/";

        Stat exists = zooKeeper.exists(root, false);

        if (exists == null) {
            zooKeeper.create(root, "ROOT".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        logger.info("clean");
    }

    /**
     * 创建节点
     */
    private static void createNode() throws Exception {
        // 同步创建持久节点
        String path1 = zooKeeper.create("/a", "A".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        logger.info("create node success, path: {}", path1);

        // 同步创建临时节点
        String path2 = zooKeeper.create("/b", "B".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        logger.info("create node success, path: {}", path2);

        // 同步创建临时顺序节点
        String path3 = zooKeeper.create("/c", "C".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info("create node success, path: {}", path3);

        // 异步创建临时节点
        String path4 = "/d";
        MyStringCallback stringCallback = new MyStringCallback();
        zooKeeper.create(path4, "D".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "create path: " + path4);
        zooKeeper.create(path4, "D".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "create path: " + path4);

        // 异步创建临时顺序节点
        String path5 = "/e";
        zooKeeper.create(path5, "E".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, stringCallback, "create path: " + path5);
    }

    /**
     * 获取子节点列表
     */
    private static void getChildrenNode() throws Exception {
        String path = "/";

        // 同步获取子节点列表
        List<String> children = zooKeeper.getChildren(path, true);
        logger.info("get children, path: {}, children: {}", path, children);

        // 异步获取子节点列表
        zooKeeper.getChildren(path, true, new MyChildren2Callback(), "get children");

        // 子节点列表变更
        String path6 = "/f";
        zooKeeper.create(path6, "F".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new MyStringCallback(), "create path: " + path6);
    }

    /**
     * 读取数据
     */
    private static void getData() throws Exception {
        String path1 = "/a";
        Stat stat = new Stat();

        // 同步读取数据
        byte[] data = zooKeeper.getData(path1, true, stat);
        logger.info("get data, path: {}, data: {}, stat: {}", path1, new String(data), stat);

        // 异步读取数据
        zooKeeper.getData(path1, true, new MyDataCallback(), "get data");

        // 变更数据
        zooKeeper.setData(path1, "AAA".getBytes(), -1);
    }

    /**
     * 更新数据
     */
    private static void setData() throws Exception {
        String path1 = "/a";

        // 同步更新数据
        Stat stat1 = zooKeeper.setData(path1, "A1".getBytes(), -1);
        logger.info("set data, path: {}, stat: {}", path1, stat1);

        // 同步更新数据
        Stat stat2 = zooKeeper.setData(path1, "A2".getBytes(), stat1.getVersion());
        logger.info("set data, path: {}, stat: {}", path1, stat2);

        // 异步更新数据
        zooKeeper.setData(path1, "A3".getBytes(), -1, new MyStatCallback(), "set data");
    }

    /**
     * 检测节点是否存在
     */
    private static void exists() throws Exception {
        String path7 = "/g";

        // 同步检测节点是否存在
        Stat stat = zooKeeper.exists(path7, true);
        logger.info("check exists, path: {}, stat: {}", path7, stat);

        // 创建节点
        zooKeeper.create(path7, "G".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 更新节点
        zooKeeper.setData(path7, "GG".getBytes(), -1);
        Thread.sleep(100);

        // 删除节点
        zooKeeper.delete(path7, -1);
    }

    /**
     * 删除节点
     */
    private static void deleteNode() throws Exception {
        String path1 = "/a";
        zooKeeper.delete(path1, -1);
        logger.info("delete node success, path: {}", path1);
    }

    /**
     * 权限控制
     */
    private static void auth() throws Exception {
        // 添加权限信息
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());

        String path8 = "/h";

        // 创建节点
        zooKeeper.create(path8, "H".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        // 使用无权限的 ZK 会话访问节点
        createSession();

        try {
            zooKeeper.getData(path8, false, null);
        } catch (Exception e) {
            logger.error("no auth");
        }
    }

    /**
     * My Watcher
     */
    private static class MyWatcher implements Watcher {
        private CountDownLatch countDownLatch;

        public MyWatcher(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            logger.info("【{}】receive watch event: {}", this, watchedEvent);

            String path = watchedEvent.getPath();
            Event.EventType type = watchedEvent.getType();

            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                // 建立连接
                if (Event.EventType.None == type && null == path) {
                    logger.info("【{}】sync connected", this);
                    countDownLatch.countDown();
                    return;
                }

                // 节点创建
                if (Event.EventType.NodeCreated == type) {
                    logger.info("【{}】node created, path: {}", this, path);

                    try {
                        zooKeeper.exists(path, true);
                    } catch (Exception e) {
                        logger.error("【{}】check exists error, path: {}", this, path, e);
                    }
                    return;
                }

                // 节点删除
                if (Event.EventType.NodeDeleted == type) {
                    logger.info("【{}】node deleted, path: {}", this, path);

                    try {
                        zooKeeper.exists(path, true);
                    } catch (Exception e) {
                        logger.error("【{}】check exists error, path: {}", this, path, e);
                    }
                    return;
                }

                // 节点数据变更
                if (Event.EventType.NodeDataChanged == type) {
                    Stat stat = new Stat();
                    try {
                        byte[] data = zooKeeper.getData(path, true, stat);
                        logger.info("【{}】node data changed, path: {}, data: {}, stat: {}", this, path, new String(data), stat);
                    } catch (Exception e) {
                        logger.error("【{}】get data error, path: {}", this, path, e);
                    }
                    return;
                }

                // 子节点列表变更
                if (Event.EventType.NodeChildrenChanged == type) {
                    try {
                        List<String> children = zooKeeper.getChildren(path, true);
                        logger.info("【{}】node children changed, path: {}, children: {}", this, path, children);
                    } catch (Exception e) {
                        logger.error("【{}】get children error", this, e);
                    }
                }
            }
        }
    }

    /**
     * My StringCallback
     */
    private static class MyStringCallback implements AsyncCallback.StringCallback {
        @Override
        public void processResult(int rc, String path, Object context, String name) {
            // rc，result code，0：Ok，-4：ConnectionLoss，-110：NodeExists，-112：SessionExpired
            logger.info("【{}】receive result, rc: {}, path: {}, context: {}, name: {}", this, rc, path, context, name);
        }
    }

    /**
     * My Children2Callback
     */
    private static class MyChildren2Callback implements AsyncCallback.Children2Callback {
        @Override
        public void processResult(int rc, String path, Object context, List<String> list, Stat stat) {
            logger.info("【{}】receive result, rc: {}, path: {}, context: {}, list: {}, stat: {}", this, rc, path, context, list, stat);
        }
    }

    /**
     * My DataCallback
     */
    private static class MyDataCallback implements AsyncCallback.DataCallback {
        @Override
        public void processResult(int rc, String path, Object context, byte[] data, Stat stat) {
            logger.info("【{}】receive result, rc: {}, path: {}, context: {}, data: {}, stat: {}", this, rc, path, context, new String(data), stat);
        }
    }

    /**
     * My StatCallback
     */
    private static class MyStatCallback implements AsyncCallback.StatCallback {
        @Override
        public void processResult(int rc, String path, Object context, Stat stat) {
            logger.info("【{}】receive result, rc: {}, path: {}, context: {}, stat: {}", this, rc, path, context, stat);
        }
    }

}
