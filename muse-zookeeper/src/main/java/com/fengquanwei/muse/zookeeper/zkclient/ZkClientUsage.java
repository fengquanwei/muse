package com.fengquanwei.muse.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ZkClient 用法
 *
 * @author fengquanwei
 * @create 2019/1/31 10:03
 **/
public class ZkClientUsage {
    private static final Logger logger = LoggerFactory.getLogger(ZkClientUsage.class);

    /**
     * ZkClient用法
     */
    public static void main(String[] args) throws Exception {
        logger.info("========== 创建会话 ==========");

        ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/zkClient", 5000);
        logger.info("创建会话成功");

        logger.info("========== 清理环境 ==========");

        String root = "/";

        if (!zkClient.exists(root)) {
            zkClient.create(root, "ROOT", CreateMode.PERSISTENT);
        }

        // 递归删除根目录下的子节点（暂未实现）

        logger.info("清理环境完成");

        logger.info("========== 创建节点 ==========");

        String path1 = "/a";
        zkClient.createPersistent(path1, "A");
        logger.info("创建节点, 节点路径: {}", path1);

        String path2 = path1 + "/aa/aaa";
        zkClient.createPersistent(path2, true);
        logger.info("递归创建节点, 节点路径: {}", path2);

        logger.info("========== 获取子节点列表 ==========");

        List<String> children = zkClient.getChildren(path1);
        logger.info("获取子节点列表, 节点路径: {}, 子节点列表: {}", path1, children);

        logger.info("========== 监听子节点变更 ==========");

        zkClient.subscribeChildChanges(path1, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
                logger.info("【IZkChildListener】处理子节点变更, 父节点路径: {}, 新的子节点列表: {}", parentPath, currentChildren);
            }
        });

        String path3 = path1 + "/A";
        zkClient.createPersistent(path3);
        logger.info("创建节点, 节点路径: {}", path3);
        Thread.sleep(100);

        zkClient.delete(path3);
        logger.info("删除节点, 节点路径: {}", path3);
        Thread.sleep(100);

        logger.info("========== 读取数据 ==========");

        Object data = zkClient.readData(path1);
        logger.info("读取数据, 节点路径: {}, 节点数据: {}", path1, data);

        logger.info("========== 监听节点变更 ==========");

        zkClient.subscribeDataChanges(path1, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                logger.info("【IZkDataListener】处理数据变更, 节点路径: {}, 最新数据: {}", dataPath, data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                logger.info("【IZkDataListener】处理节点删除, 节点路径: {}", dataPath);
            }
        });

        logger.info("========== 更新数据 ==========");

        String newData = "AAA";
        zkClient.writeData(path1, newData);
        logger.info("更新数据, 节点路径: {}, 新节点数据: {}", path1, newData);
        Thread.sleep(100);

        logger.info("========== 检测节点是否存在 ==========");

        boolean exists = zkClient.exists(path1);
        logger.info("检测节点是否存在, 节点路径: {}, 是否存在: {}", path1, exists);

        logger.info("========== 删除节点 ==========");

        zkClient.deleteRecursive(path1);
        logger.info("递归删除节点, 节点路径: {}", path1);

        Thread.sleep(100000000);
    }
}
