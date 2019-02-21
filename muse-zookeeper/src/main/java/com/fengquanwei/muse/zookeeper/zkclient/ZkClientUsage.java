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
    private static final Logger logger = LoggerFactory.getLogger(ZkClientUsage.class);

    /**
     * ZkClient用法
     */
    public static void main(String[] args) throws Exception {
        logger.info("========== 创建会话 ==========");

        ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183/zkClient", 5000);
        logger.info("connect zookeeper success");

        logger.info("========== 创建节点 ==========");

        String path1 = "/a";
        zkClient.createPersistent(path1, "A");
        logger.info("create node, path: {}", path1);

        String path2 = path1 + "/aa/aaa";
        zkClient.createPersistent(path2, true);
        logger.info("create node, path: {}", path2);

        logger.info("========== 获取子节点列表 ==========");

        List<String> children = zkClient.getChildren(path1);
        logger.info("get children, path: {}, children: {}", path1, children);

        logger.info("========== 监听子节点变更 ==========");

        zkClient.subscribeChildChanges(path1, (String parentPath, List<String> currentChildren) -> logger.info("handle child change, parentPath: {}, currentChildren: {}", parentPath, currentChildren));

        String path3 = path1 + "/A";
        zkClient.createPersistent(path3);
        Thread.sleep(100);
        zkClient.delete(path3);

        Thread.sleep(100);

        logger.info("========== 读取数据 ==========");

        Object data = zkClient.readData(path1);
        logger.info("read data, path: {}, data: {}", path1, data);

        logger.info("========== 监听节点变更 ==========");

        zkClient.subscribeDataChanges(path1, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                logger.info("handle data change, dataPath: {}, data: {}", dataPath, data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                logger.info("handle data deleted, dataPath: {}", dataPath);
            }
        });

        logger.info("========== 更新数据 ==========");

        String newData = "AAA";
        zkClient.writeData(path1, newData);
        logger.info("write data, path: {}, data: {}", path1, newData);

        logger.info("========== 检测节点是否存在 ==========");

        boolean exists = zkClient.exists(path1);
        logger.info("exists, path: {}, exists: {}", path1, exists);

        logger.info("========== 删除节点 ==========");

        zkClient.deleteRecursive(path1);
        logger.info("delete node, path: {}", path1);
    }
}
