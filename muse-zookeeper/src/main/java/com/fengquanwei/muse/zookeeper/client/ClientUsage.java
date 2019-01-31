package com.fengquanwei.muse.zookeeper.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    /**
     * 客户端用法
     */
    public static void main(String[] args) {
        logger.info("========== 创建会话 ==========");
        ZooKeeper zooKeeper = createSession();

        logger.info("========== 复用会话 ==========");
        reuseSession(1, "wrongPasswd".getBytes());

        logger.info("========== 复用会话 ==========");
        reuseSession(zooKeeper.getSessionId(), zooKeeper.getSessionPasswd());

        sleep(5000);
    }

    /**
     * 创建会话
     */
    private static ZooKeeper createSession() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        int sessionTimeout = 5000;
        MyWatcher watcher = new MyWatcher(countDownLatch);

        // 创建 zk 实例
        ZooKeeper zooKeeper;
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
        } catch (IOException e) {
            logger.error("connect zookeeper error, connectString: {}, sessionTimeout: {}, watcher: {}", connectString, sessionTimeout, watcher, e);
            return null;
        }

        // 等待 zk 连接
        boolean reached;
        try {
            reached = countDownLatch.await(6000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("count down latch await error", e);
            return null;
        }

        // 打印连接状态
        if (reached) {
            logger.info("connect zookeeper success");
            return zooKeeper;
        } else {
            logger.error("connect zookeeper timeout");
            return null;
        }
    }

    /**
     * 复用会话
     */
    private static ZooKeeper reuseSession(long sessionId, byte[] sessionPasswd) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        int sessionTimeout = 5000;
        MyWatcher watcher = new MyWatcher(countDownLatch);

        // 创建 zk 实例
        ZooKeeper zooKeeper;
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher, sessionId, sessionPasswd);
        } catch (IOException e) {
            logger.error("connect zookeeper error, connectString: {}, sessionTimeout: {}, watcher: {}, sessionId: {}, sessionPasswd: {}", connectString, sessionTimeout, watcher, sessionId, sessionPasswd, e);
            return null;
        }

        // 等待 zk 连接
        boolean reached;
        try {
            reached = countDownLatch.await(6000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("count down latch await error", e);
            return null;
        }

        // 打印连接状态
        if (reached) {
            logger.info("connect zookeeper success");
            return zooKeeper;
        } else {
            logger.error("connect zookeeper timeout");
            return null;
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
            logger.info("{} receive watch event: {}", this, watchedEvent);
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                countDownLatch.countDown();
            }
        }
    }
}
