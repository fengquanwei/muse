package com.fengquanwei.muse.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 典型使用场景
 *
 * @author fengquanwei
 * @create 2019/2/13 18:21
 **/
public class RecipesUsage {
    private static final Logger logger = LoggerFactory.getLogger(RecipesUsage.class);

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
                .namespace("recipes")
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

        logger.info("========== 事件监听 - 节点监听 ==========");

        String path1 = "/a";

        // 节点监听
        NodeCache nodeCache = new NodeCache(client, path1, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(() -> {
                    ChildData currentData = nodeCache.getCurrentData();
                    if (currentData != null) {
                        logger.info("node changed, path: {}, data: {}, stat: {}", path1, new String(currentData.getData()), currentData.getStat());
                    } else {
                        logger.info("node changed, path: {}, data: {}, stat: {}", path1, null, null);
                    }
                }
        );

        // 节点变更
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path1, "A".getBytes());
        Thread.sleep(100);
        client.setData().forPath(path1, "AA".getBytes());
        Thread.sleep(100);
        client.delete().forPath(path1);
        Thread.sleep(100);

        logger.info("========== 事件监听 - 子节点监听 ==========");

        String path2 = "/b";
        client.create().withMode(CreateMode.PERSISTENT).forPath(path2);

        // 子节点监听
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path2, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getListenable().addListener((CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) -> {
                    logger.info("child event: {}", pathChildrenCacheEvent);
                }
        );

        // 子节点变更
        String subPath = path2 + "/b1";
        client.create().withMode(CreateMode.PERSISTENT).forPath(subPath);
        Thread.sleep(100);
        client.setData().forPath(subPath, "B1".getBytes());
        Thread.sleep(100);
        client.delete().forPath(subPath);
        Thread.sleep(100);
        client.delete().forPath(path2);
        Thread.sleep(100);

        logger.info("========== Master 选举 ==========");

        String masterPath = "/master";

        LeaderSelector leaderSelector1 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                logger.info("leaderSelector1, take leadership");

                Thread.sleep(1000);

                logger.info("leaderSelector1, release leadership");
            }
        });
        leaderSelector1.autoRequeue();
        leaderSelector1.start();

        LeaderSelector leaderSelector2 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                logger.info("leaderSelector2, take leadership");

                Thread.sleep(2000);

                logger.info("leaderSelector2, release leadership");
            }
        });
        leaderSelector2.autoRequeue();
        leaderSelector2.start();

        LeaderSelector leaderSelector3 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                logger.info("leaderSelector3, take leadership");

                Thread.sleep(3000);

                logger.info("leaderSelector3, release leadership");
            }
        });
        leaderSelector3.autoRequeue();
        leaderSelector3.start();

        Thread.sleep(6000);
        leaderSelector1.close();
        leaderSelector2.close();
        leaderSelector3.close();

        logger.info("========== 分布式锁 ==========");

        String lockPath = "/lock";
        final CountDownLatch lockCountDownLatch = new CountDownLatch(1);

        // 分布式锁
        final InterProcessMutex lock = new InterProcessMutex(client, lockPath);

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                try {
                    // 预备
                    lockCountDownLatch.await();
                    // 获取锁
                    lock.acquire();
                } catch (Exception e) {
                    logger.error("acquire lock error", e);
                }

                String orderNo = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
                logger.info("order no: {}", orderNo);

                // 释放锁
                try {
                    lock.release();
                } catch (Exception e) {
                    logger.error("release lock error", e);
                }
            }).start();
        }

        // 开始
        lockCountDownLatch.countDown();

        Thread.sleep(1000);

        logger.info("========== 分布式计数器 ==========");

        String counterPath = "/counter";
        final CountDownLatch counterCountDownLatch = new CountDownLatch(10);

        // 分布式计数器
        final DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(client, counterPath, new RetryNTimes(3, 1000));

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                AtomicValue<Integer> atomicValue = null;

                while (atomicValue == null || !atomicValue.succeeded()) {
                    try {
                        atomicValue = distributedAtomicInteger.add(1);
                        logger.info("add succeeded: {}, pre value: {}, post value: {}", atomicValue.succeeded(), atomicValue.preValue(), atomicValue.postValue());
                    } catch (Exception e) {
                        logger.error("add error", e);
                    }
                }

                counterCountDownLatch.countDown();
            }).start();
        }

        counterCountDownLatch.await();

        logger.info("========== 分布式 Barrier ==========");

        final String barrierPath = "/barrier";

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Worker("1 号选手", barrierPath, client));
        executorService.submit(new Worker("2 号选手", barrierPath, client));
        executorService.submit(new Worker("3 号选手", barrierPath, client));
        executorService.shutdown();
    }

    /**
     * 分布式 barrier 工人
     */
    static class Worker implements Runnable {
        private String name;
        private String path;
        private CuratorFramework client;

        public Worker(String name, String path, CuratorFramework client) {
            this.name = name;
            this.path = path;
            this.client = client;
        }

        @Override
        public void run() {
            try {
                DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, path, 3);
                logger.info(this.name + " ready");
                barrier.enter();
                logger.info(this.name + " enter");
                barrier.leave();
                logger.info(this.name + " leave");
            } catch (Exception e) {
                logger.error("await error", e);
            }
        }
    }
}
