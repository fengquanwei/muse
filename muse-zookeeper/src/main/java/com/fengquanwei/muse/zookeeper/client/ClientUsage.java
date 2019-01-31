package com.fengquanwei.muse.zookeeper.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    private static Logger logger = LoggerFactory.getLogger(ClientUsage.class);

    // zk 客户端
    private static ZooKeeper zooKeeper;

    /**
     * 客户端用法
     */
    public static void main(String[] args) {
        logger.info("========== 创建会话 ==========");
        createSession();
        sleep(1000);

//        long sessionId = zooKeeper.getSessionId();
//        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
//
//        logger.info("========== 复用会话 ==========");
//        reuseSession(1, "wrongPasswd".getBytes());
//        sleep(1000);
//
//        logger.info("========== 复用会话 ==========");
//        reuseSession(sessionId, sessionPasswd);
//        sleep(1000);

        logger.info("========== 创建节点 ==========");
        createNode();
        sleep(1000);

        logger.info("========== 获取子节点列表 ==========");
        getChildrenNode();
        sleep(1000);

        logger.info("========== 读取数据 ==========");
        getData();
        sleep(1000);

        logger.info("========== 删除节点 ==========");
        deleteNode();
        sleep(1000);
    }

    /**
     * 创建会话
     */
    private static void createSession() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/test";
        int sessionTimeout = 5000;
        MyWatcher watcher = new MyWatcher(countDownLatch);

        // 创建 zk 实例
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
        } catch (IOException e) {
            logger.error("connect zookeeper error, connectString: {}, sessionTimeout: {}, watcher: {}", connectString, sessionTimeout, watcher, e);
            return;
        }

        // 等待 zk 连接
        boolean reached;
        try {
            reached = countDownLatch.await(6000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("count down latch await error", e);
            return;
        }

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
    private static void reuseSession(long sessionId, byte[] sessionPasswd) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/test";
        int sessionTimeout = 5000;
        MyWatcher watcher = new MyWatcher(countDownLatch);

        // 创建 zk 实例
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher, sessionId, sessionPasswd);
        } catch (IOException e) {
            logger.error("connect zookeeper error, connectString: {}, sessionTimeout: {}, watcher: {}, sessionId: {}, sessionPasswd: {}", connectString, sessionTimeout, watcher, sessionId, sessionPasswd, e);
            return;
        }

        // 等待 zk 连接
        boolean reached;
        try {
            reached = countDownLatch.await(6000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("count down latch await error", e);
            return;
        }

        // 打印连接状态
        if (reached) {
            logger.info("connect zookeeper success");
        } else {
            logger.error("connect zookeeper timeout");
        }
    }

    /**
     * 创建节点
     */
    private static void createNode() {
        // 同步创建持久节点
        try {
            String path = zooKeeper.create("/t", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("create node success, path: {}", path);
        } catch (Exception e) {
            logger.error("create node error", e);
        }

        // 同步创建临时节点
        try {
            String path = zooKeeper.create("/t1", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("create node success, path: {}", path);
        } catch (Exception e) {
            logger.error("create node error", e);
        }

        // 同步创建临时顺序节点
        try {
            String path = zooKeeper.create("/t2", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create node success, path: {}", path);
        } catch (Exception e) {
            logger.error("create node error", e);
        }

        // 异步创建临时节点
        MyStringCallback stringCallback = new MyStringCallback();
        zooKeeper.create("/t3", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "create path: /t3");
        zooKeeper.create("/t3", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "create path: /t3");

        // 异步创建临时顺序节点
        zooKeeper.create("/t4", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, stringCallback, "create path: /t4");
    }

    /**
     * 获取子节点列表
     */
    private static void getChildrenNode() {
        try {
            String path = "/";

            // 同步获取子节点列表
            List<String> children = zooKeeper.getChildren(path, true);
            logger.info("get children, path: {}, children: {}", path, children);

            // 异步获取子节点列表
            zooKeeper.getChildren(path, true, new MyChildren2Callback(), "get children");

            // 子节点列表变更
            zooKeeper.create("/t5", "T".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new MyStringCallback(), "create path: /t5");
        } catch (Exception e) {
            logger.error("get children error", e);
        }
    }

    /**
     * 读取数据
     */
    private static void getData() {
        String path = "/t";
        Stat stat = new Stat();
        try {
            // 同步读取数据
            byte[] data = zooKeeper.getData(path, true, stat);
            logger.info("get data, path: {}, data: {}, stat: {}", path, new String(data), stat);

            // 异步读取数据
            zooKeeper.getData(path, true, new MyDataCallback(), "get data");

            // 变更数据
            zooKeeper.setData(path, "TEST".getBytes(), -1);
        } catch (Exception e) {
            logger.error("get data error, path: {}", path, e);
        }
    }

    /**
     * 删除节点
     */
    private static void deleteNode() {
        try {
            String path = "/t";
            zooKeeper.delete(path, 1);
            logger.info("delete node success, path: {}", path);
        } catch (Exception e) {
            logger.error("delete node error", e);
        }
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
                    countDownLatch.countDown();
                    return;
                }

                // 子节点列表变更
                if (Event.EventType.NodeChildrenChanged == type) {
                    try {
                        List<String> children = zooKeeper.getChildren(path, true);
                        logger.info("【{}】get children, path: {}, children: {}", this, path, children);
                    } catch (Exception e) {
                        logger.error("【{}】get children error", this, e);
                    }
                }

                // 数据变更
                if (Event.EventType.NodeDataChanged == type) {
                    Stat stat = new Stat();
                    try {
                        byte[] data = zooKeeper.getData(path, true, stat);
                        logger.info("【{}】get data, path: {}, data: {}, stat: {}", this, path, new String(data), stat);
                    } catch (Exception e) {
                        logger.error("【{}】get data error, path: {}", this, path, e);
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

}
