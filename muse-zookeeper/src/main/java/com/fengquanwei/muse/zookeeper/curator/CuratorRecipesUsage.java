package com.fengquanwei.muse.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
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
 * Curator 典型使用场景
 *
 * @author fengquanwei
 * @create 2019/2/13 18:21
 **/
public class CuratorRecipesUsage {
    private static final Logger logger = LoggerFactory.getLogger(CuratorRecipesUsage.class);

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

        logger.info("创建会话成功");

        logger.info("========== 清理环境 ==========");

        // 删除根节点及其所有子节点
        client.delete().deletingChildrenIfNeeded().forPath("");

        // 创建根节点
        client.create().forPath("");

        logger.info("清理环境完成");

        logger.info("========== 事件监听 - 节点监听 ==========");

        String path1 = "/a";

        // 节点监听
        NodeCache nodeCache = new NodeCache(client, path1, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData currentData = nodeCache.getCurrentData();
                if (currentData != null) {
                    logger.info("【NodeCacheListener】处理节点变更, 节点路径: {}, 节点数据: {}, 节点状态: {}", path1, new String(currentData.getData()), currentData.getStat());
                } else {
                    logger.info("【NodeCacheListener】处理节点变更, 节点路径: {}, 节点数据: {}, 节点状态: {}", path1, null, null);
                }
            }
        });

        // 节点变更
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path1, "A".getBytes());
        logger.info("创建节点, 节点路径: {}", path1);
        Thread.sleep(100);
        client.setData().forPath(path1, "AA".getBytes());
        logger.info("更新节点, 节点路径: {}", path1);
        Thread.sleep(100);
        client.delete().forPath(path1);
        logger.info("删除节点, 节点路径: {}", path1);
        Thread.sleep(100);

        logger.info("========== 事件监听 - 子节点监听 ==========");

        String path2 = "/b";

        // 子节点监听
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path2, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                PathChildrenCacheEvent.Type type = event.getType();
                ChildData childData = event.getData();

                if (childData != null) {
                    String path = childData.getPath();
                    byte[] data = childData.getData();
                    logger.info("【PathChildrenCacheListener】处理子节点变更, 节点路径: {}, 事件类型: {}, 子节点路径: {}, 子节点数据: {}", path2, type, path, new String(data));
                } else {
                    logger.info("【PathChildrenCacheListener】处理子节点变更, 节点路径: {}, 事件类型: {}, 子节点路径: {}, 子节点数据: {}", path2, type, null, null);
                }
            }
        });

        // 子节点变更
        client.create().withMode(CreateMode.PERSISTENT).forPath(path2);
        logger.info("创建节点, 节点路径: {}", path2);
        String subPath = path2 + "/b1";
        client.create().withMode(CreateMode.PERSISTENT).forPath(subPath);
        logger.info("创建节点, 节点路径: {}", subPath);
        Thread.sleep(100);
        client.setData().forPath(subPath, "B1".getBytes());
        logger.info("更新节点, 节点路径: {}", subPath);
        Thread.sleep(100);
        client.delete().forPath(subPath);
        logger.info("删除节点, 节点路径: {}", subPath);
        Thread.sleep(100);
        client.delete().forPath(path2);
        logger.info("删除节点, 节点路径: {}", path2);
        Thread.sleep(100);

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
                    logger.error("获取锁异常", e);
                }

                String orderNo = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
                logger.info("订单号: {}", orderNo);

                // 释放锁
                try {
                    lock.release();
                } catch (Exception e) {
                    logger.error("释放锁异常", e);
                }
            }).start();
        }

        // 开始
        lockCountDownLatch.countDown();

        Thread.sleep(1000);

        logger.info("========== Master 选举 ==========");

        String masterPath = "/master";

//        PathChildrenCache masterPathChildCache = new PathChildrenCache(client, masterPath, true);
//        masterPathChildCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
//        masterPathChildCache.getListenable().addListener(new PathChildrenCacheListener() {
//            @Override
//            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
//                PathChildrenCacheEvent.Type type = event.getType();
//                ChildData childData = event.getData();
//
//                if (childData != null) {
//                    String path = childData.getPath();
//                    byte[] data = childData.getData();
//                    logger.info("【PathChildrenCacheListener】处理子节点变更, 节点路径: {}, 事件类型: {}, 子节点路径: {}, 子节点数据: {}", masterPath, type, path, new String(data));
//                } else {
//                    logger.info("【PathChildrenCacheListener】处理子节点变更, 节点路径: {}, 事件类型: {}, 子节点路径: {}, 子节点数据: {}", masterPath, type, null, null);
//                }
//
//            }
//        });

        LeaderSelector leaderSelector1 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                try {
                    logger.info("一号机获取 Leader 权利");

                    Thread.sleep(1000);

                    logger.info("一号机释放 Leader 权利");
                } catch (Exception e) {
                    logger.error("一号机执行任务异常", e);
                } finally {
                    logger.info("一号机执行任务结束");
                }
            }
        });
        leaderSelector1.autoRequeue();
        leaderSelector1.start();

        LeaderSelector leaderSelector2 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                try {
                    logger.info("二号机获取 Leader 权利");

                    Thread.sleep(2000);

                    logger.info("二号机释放 Leader 权利");
                } catch (Exception e) {
                    logger.error("二号机执行任务异常", e);
                } finally {
                    logger.info("二号机执行任务结束");
                }
            }
        });
        leaderSelector2.autoRequeue();
        leaderSelector2.start();

        LeaderSelector leaderSelector3 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                try {
                    logger.info("三号机获取 Leader 权利");

                    Thread.sleep(3000);

                    logger.info("三号机释放 Leader 权利");
                } catch (Exception e) {
                    logger.error("三号机执行任务异常", e);
                } finally {
                    logger.info("三号机执行任务结束");
                }
            }
        });
        leaderSelector3.autoRequeue();
        leaderSelector3.start();

        LeaderSelector leaderSelector4 = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                try {
                    logger.info("四号机获取 Leader 权利");

                    Thread.sleep(4000);

                    logger.info("四号机释放 Leader 权利");
                } catch (Exception e) {
                    logger.error("四号机执行任务异常", e);
                } finally {
                    logger.info("四号机执行任务结束");
                }
            }
        });
        leaderSelector4.autoRequeue();
        leaderSelector4.start();

        Thread.sleep(10000);
        leaderSelector1.close();
        leaderSelector2.close();
        leaderSelector3.close();
        leaderSelector4.close();
        Thread.sleep(100);

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
                        logger.info("计数成功: {}, 前值: {}, 后值: {}", atomicValue.succeeded(), atomicValue.preValue(), atomicValue.postValue());
                    } catch (Exception e) {
                        logger.error("计数异常", e);
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

        Thread.sleep(1000000000);
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
                logger.info(this.name + ": 准备");
                barrier.enter();
                logger.info(this.name + ": 进入");
                barrier.leave();
                logger.info(this.name + ": 离开");
            } catch (Exception e) {
                logger.error("异常", e);
            }
        }
    }
}
