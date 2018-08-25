package com.example.demo.controller;

import com.example.demo.domain.Node;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ZK")
public class ZkController {
    private static ZkClient zkClient = new ZkClient("127.0.0.1:2181", 1000, 1000, new SerializableSerializer());

    /**
     * 创建节点  节点需要一层一层的创建,不然报错
     *
     * @return
     */
    @RequestMapping("/createNode")
    public String createNode() {
        List<Node> nodes = new ArrayList<>();
        Node node = new Node("192.168.1.1", 45);
        nodes.add(node);
        zkClient.create("/com/ser/s", nodes, CreateMode.PERSISTENT);
        return "success";
    }

    /**
     * 创建多层节点
     *
     * @return
     */
    @RequestMapping("/createManyNode")
    public String createManyNode() {
        zkClient.createPersistent("/com/ser/s", true);
        return "success";
    }

    /**
     * 获取节点的数据   并修改节点的数据
     *
     * @return
     */
    @RequestMapping("/getNodeData")
    public String getNodeData() {
        Stat stat = new Stat();
        List<Node> nodes = (List<Node>) zkClient.readData("/com", stat);
        for (Node node : nodes) {
            System.out.println(node.getHost() + "---" + node.getPort());
        }
        nodes.add(new Node("129.23.34.45", 90));
        zkClient.writeData("/com/service", nodes);
        return "success";
    }

    /**
     * 获取孩子节点
     *
     * @return
     */
    @RequestMapping("/getChildNode")
    public String getChildNode() {
        List<String> children = zkClient.getChildren("/com");
        for (String s : children) {
            System.out.println(s);
        }
        return "success";
    }

    /**
     * 监听节点数据的变化
     *
     * @return
     */
    @RequestMapping("/listenDataChange")
    public String listenDataChange() throws InterruptedException {
        zkClient.subscribeDataChanges("/com", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("handleDataChange" + s + "----" + o);
            }
            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("handleDataDeleted " + s);
            }
        });

        Thread.sleep(Integer.MAX_VALUE);
        return "success";
    }


    /**
     * 监听直接孩子节点的变更(不包含孩子节点的数据变更)
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/listenChildChange")
    public String listenChildChange() throws InterruptedException {
        zkClient.subscribeChildChanges("/com", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s + "----" + list);
            }
        });
        Thread.sleep(Integer.MAX_VALUE);
        return "success";
    }


    /**
     * delete()  只允许删除叶子节点
     * deleteRecursive()  删除某一节点下的所有节点  递归
     *
     * @return
     */
    @RequestMapping("/deleteRecursive")
    public String deleteNode() {
        zkClient.deleteRecursive("/com");
        return "success";
    }
}
