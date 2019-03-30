package com.fengquanwei.muse.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
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

//        logger.info("========== 复用会话（错误密码） ==========");
//        reuseSession(1, "wrongPasswd".getBytes());
//        Thread.sleep(1000);
//
//        logger.info("========== 复用会话（正确密码）==========");
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

        Thread.sleep(10000000);
    }

    /**
     * 创建会话
     */
    private static void createSession() throws Exception {
        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/client";
        int sessionTimeout = 5000;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MyWatcher myWatcher = new MyWatcher(countDownLatch);

        // 创建 zk 实例
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, myWatcher);

        // 等待 zk 连接
        countDownLatch.await(5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 复用会话
     */
    private static void reuseSession(long sessionId, byte[] sessionPasswd) throws Exception {
        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/client";
        int sessionTimeout = 5000;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MyWatcher myWatcher = new MyWatcher(countDownLatch);

        // 创建 zk 实例
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, myWatcher, sessionId, sessionPasswd);

        // 等待 zk 连接
        countDownLatch.await(6000, TimeUnit.MILLISECONDS);
    }

    /**
     * 清理环境
     */
    private static void clean() throws Exception {
        String root = "/";

        // 根目录不存在则创建根目录
        Stat stat = zooKeeper.exists(root, false);

        if (stat == null) {
            zooKeeper.create(root, "ROOT".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 递归删除根目录下的子节点（暂未实现）

        logger.info("清理环境完成");
    }

    /**
     * 创建节点
     */
    private static void createNode() throws Exception {
        // 同步创建持久节点
        String path1 = zooKeeper.create("/a", "A".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        logger.info("同步创建持久节点, 节点路径: {}", path1);

        // 同步创建临时节点
        String path2 = zooKeeper.create("/b", "B".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        logger.info("同步创建临时节点, 节点路径: {}", path2);

        // 同步创建临时顺序节点
        String path3 = zooKeeper.create("/c", "C".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info("同步创建临时顺序节点, 节点路径: {}", path3);

        // 异步创建临时节点
        String path4 = "/d";
        MyStringCallback stringCallback = new MyStringCallback();
        zooKeeper.create(path4, "D".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "异步创建临时节点");
        zooKeeper.create(path4, "D".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, stringCallback, "异步创建临时节点");

        // 异步创建临时顺序节点
        String path5 = "/e";
        zooKeeper.create(path5, "E".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, stringCallback, "异步创建临时顺序节点");
    }

    /**
     * 获取子节点列表
     */
    private static void getChildrenNode() throws Exception {
        String root = "/";

        // 同步获取子节点列表
        List<String> children = zooKeeper.getChildren(root, true);
        logger.info("同步获取子节点列表, 节点路径: {}, 子节点列表: {}", root, children);

        // 异步获取子节点列表
        zooKeeper.getChildren(root, true, new MyChildren2Callback(), "异步获取子节点列表");

        // 子节点列表变更
        String path6 = "/f";
        zooKeeper.create(path6, "F".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        logger.info("同步创建临时节点, 节点路径: {}", path6);
    }

    /**
     * 读取数据
     */
    private static void getData() throws Exception {
        String path1 = "/a";

        // 同步读取数据
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData(path1, true, stat);
        logger.info("同步读取数据, 节点路径: {}, 节点数据: {}, 节点状态: {}", path1, new String(data), stat);

        // 异步读取数据
        zooKeeper.getData(path1, true, new MyDataCallback(), "异步读取数据");

        // 更新数据
        zooKeeper.setData(path1, "AAA".getBytes(), -1);
        logger.info("同步更新数据, 节点路径: {}", path1);
    }

    /**
     * 更新数据
     */
    private static void setData() throws Exception {
        String path1 = "/a";

        // 同步更新数据
        String newData1 = "A1";
        Stat stat1 = zooKeeper.setData(path1, newData1.getBytes(), -1);
        logger.info("同步更新数据, 节点路径: {}, 节点新数据: {}, 节点新状态: {}", path1, newData1, stat1);

        // 同步更新数据
        String newData2 = "A2";
        Stat stat2 = zooKeeper.setData(path1, newData2.getBytes(), stat1.getVersion());
        logger.info("同步更新数据, 节点路径: {}, 节点新数据: {}, 节点新状态: {}", path1, newData2, stat2);

        // 异步更新数据
        zooKeeper.setData(path1, "A3".getBytes(), -1, new MyStatCallback(), "异步更新数据");
    }

    /**
     * 检测节点是否存在
     */
    private static void exists() throws Exception {
        String path7 = "/g";

        // 同步检测节点是否存在
        Stat stat = zooKeeper.exists(path7, true);
        logger.info("同步检测节点是否存在, 节点路径: {}, 节点状态: {}", path7, stat);

        // 创建节点
        zooKeeper.create(path7, "G".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        logger.info("同步创建临时节点, 节点路径: {}", path7);

        // 更新节点
        zooKeeper.setData(path7, "GG".getBytes(), -1);
        logger.info("同步更新节点, 节点路径: {}", path7);

        // 删除节点
        zooKeeper.delete(path7, -1);
        logger.info("同步删除节点, 节点路径: {}", path7);
    }

    /**
     * 删除节点
     */
    private static void deleteNode() throws Exception {
        // 同步删除节
        String path1 = "/a";
        zooKeeper.delete(path1, -1);
        logger.info("同步删除节点, 节点路径: {}", path1);

        // 异步删除节点
        String path2 = "/b";
        zooKeeper.delete(path2, -1, new MyVoidCallback(), "异步删除节点");
    }

    /**
     * 权限控制
     */
    private static void auth() throws Exception {
        // 添加权限信息
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        logger.info("添加权限信息");

        String path8 = "/h";

        // 创建节点
        zooKeeper.create(path8, "H".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        logger.info("同步创建临时节点");

        // 使用无权限的 ZK 会话访问节点
        createSession();

        try {
            zooKeeper.getData(path8, false, null);
        } catch (Exception e) {
            logger.error("使用无权限的客户端访问节点异常", e);
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
            Event.KeeperState state = watchedEvent.getState();
            Event.EventType type = watchedEvent.getType();
            String path = watchedEvent.getPath();

            logger.info("【MyWatcher】收到通知, 通知状态：{}, 事件类型: {}, 节点路径: {}", state, type, path);

            try {
                // 节点创建
                if (Event.KeeperState.SyncConnected == state && Event.EventType.NodeCreated == type) {
                    logger.info("【MyWatcher】处理通知: 节点创建, 节点路径: {}", path);
                    zooKeeper.exists(path, true);
                    return;
                }

                // 节点删除
                if (Event.KeeperState.SyncConnected == state && Event.EventType.NodeDeleted == type) {
                    logger.info("【MyWatcher】处理通知: 节点删除, 节点路径: {}", path);
                    zooKeeper.exists(path, true);
                    return;
                }

                // 节点数据变更
                if (Event.KeeperState.SyncConnected == state && Event.EventType.NodeDataChanged == type) {
                    Stat stat = new Stat();
                    byte[] data = zooKeeper.getData(path, true, stat);
                    logger.info("【MyWatcher】处理通知: 节点数据变更, 节点路径: {}, 节点新数据: {}, 节点新状态: {}", path, new String(data), stat);
                    return;
                }

                // 子节点列表变更
                if (Event.KeeperState.SyncConnected == state && Event.EventType.NodeChildrenChanged == type) {
                    List<String> children = zooKeeper.getChildren(path, true);
                    logger.info("【MyWatcher】处理通知: 子节点列表变更, 节点路径: {}, 子节点新列表: {}", path, children);
                    return;
                }

                // 会话建立
                if (state == Event.KeeperState.SyncConnected && type == Event.EventType.None) {
                    logger.info("【MyWatcher】处理通知: 会话建立");
                    countDownLatch.countDown();
                    return;
                }

                // 会话断开
                if (state == Event.KeeperState.Disconnected && type == Event.EventType.None) {
                    logger.info("【MyWatcher】处理通知: 会话断开");
                    return;
                }

                // 会话过期
                if (state == Event.KeeperState.Expired && type == Event.EventType.None) {
                    logger.info("【MyWatcher】处理通知: 会话过期");
                    return;
                }

                // 授权失败
                if (state == Event.KeeperState.AuthFailed && type == Event.EventType.None) {
                    logger.info("【MyWatcher】处理通知: 授权失败");
                    return;
                }
            } catch (Exception e) {
                logger.error("【MyWatcher】处理通知异常, 通知状态：{}, 事件类型: {}, 节点路径: {}, 异常信息: {}", state, type, path, e);
            }
        }
    }

    /**
     * 异步调用响应码: rc, result code
     * 0：Ok
     * -4：ConnectionLos
     * -110：NodeExist
     * -112：SessionExpired
     */

    /**
     * My StringCallback
     */
    private static class MyStringCallback implements AsyncCallback.StringCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            logger.info("【MyStringCallback】处理异步结果, 响应码: {}, 节点路径: {}, 上下文: {}, 实际节点路径: {}", rc, path, ctx, name);
        }
    }

    /**
     * MyVoidCallback
     */
    private static class MyVoidCallback implements AsyncCallback.VoidCallback {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            logger.info("【MyVoidCallback】处理异步结果, 响应码: {}, 节点路径: {}, 上下文: {}", rc, path, ctx);
        }
    }

    /**
     * My Children2Callback
     */
    private static class MyChildren2Callback implements AsyncCallback.Children2Callback {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> list, Stat stat) {
            logger.info("【MyChildren2Callback】处理异步结果, 响应码: {}, 节点路径: {}, 上下文: {}, 子节点列表: {}, 节点状态: {}", rc, path, ctx, list, stat);
        }
    }

    /**
     * My DataCallback
     */
    private static class MyDataCallback implements AsyncCallback.DataCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            logger.info("【MyDataCallback】处理异步结果, 响应码: {}, 节点路径: {}, 上下文: {}, 节点数据: {}, 节点状态: {}", rc, path, ctx, new String(data), stat);
        }
    }

    /**
     * My StatCallback
     */
    private static class MyStatCallback implements AsyncCallback.StatCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            logger.info("【MyStatCallback】处理异步结果, 响应码: {}, 节点路径: {}, 上下文: {}, 节点状态: {}", rc, path, ctx, stat);
        }
    }

}
