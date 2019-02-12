package com.fengquanwei.muse.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
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
    private static Logger logger = LoggerFactory.getLogger(ZkClientUsage.class);

    /**
     * ZkClient用法
     */
    public static void main(String[] args) {
        logger.info("========== 创建会话 ==========");

        ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/test", 5000);
        logger.info("connect zookeeper success");

        sleep(1000);

        logger.info("========== 创建节点 ==========");

        String root = "/r";
        zkClient.createPersistent(root, "R");
        logger.info("create node, path: {}", root);

        String path = root + "/a/b/c/d/e";
        zkClient.createPersistent(path, true);
        logger.info("create node, path: {}", path);

        sleep(1000);

        logger.info("========== 获取子节点列表 ==========");

        List<String> children = zkClient.getChildren(root);
        logger.info("get children, path: {}, children: {}", root, children);

        sleep(1000);

        logger.info("========== 监听子节点变更 ==========");

        zkClient.subscribeChildChanges(root, (String parentPath, List<String> currentChilds) -> logger.info("handle child change, parentPath: {}, currentChilds: {}", parentPath, currentChilds));

        zkClient.createPersistent(root + "/b");
        sleep(100);
        zkClient.delete(root + "/b");

        sleep(1000);

        logger.info("========== 读取数据 ==========");

        Object data = zkClient.readData(root);
        logger.info("read data, path: {}, data: {}", root, data);

        sleep(1000);

        logger.info("========== 监听节点变更 ==========");

        zkClient.subscribeDataChanges(root, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                logger.info("handle data change, dataPath: {}, data: {}", dataPath, data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                logger.info("handle data deleted, dataPath: {}", dataPath);
            }
        });

        sleep(1000);

        logger.info("========== 更新数据 ==========");

        String newData = "ROOT";
        zkClient.writeData(root, newData);
        logger.info("write data, path: {}, data: {}", root, newData);

        sleep(1000);

        logger.info("========== 检测节点是否存在 ==========");

        boolean exists = zkClient.exists(root);
        logger.info("exists, path: {}, exists: {}", root, exists);

        sleep(1000);

        logger.info("========== 删除节点 ==========");

        zkClient.deleteRecursive(root);
        logger.info("delete node, path: {}", root);

        sleep(1000);
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
}
