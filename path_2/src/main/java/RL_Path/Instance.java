package RL_Path;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;


/*
**  Create by: WSKH0929
    Date:2021-11-10
    Time:21:24
*/
@Data
public class Instance {
    // 0是路 1是墙 2是起点 3是终点
    private int[][] map = new int[][]{
            {1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1},
            {0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0},
    };
    private int startIndex, endIndex;

    private int network_node = 7;
    private int[][] network_link = {{0, 1}, {0, 6}, {1, 2}, {1, 5}, {3, 4}, {4, 5}};
    //n X n的网络，是标准二维数组
    private int[][] network_full;
    //节点间是否可达的信息，非标准二维数组
    private int[][] network_reachable_node;
    //节点之间的奖励值，非标准二维数组
    private double[][] network_rewardOfLinks;
    private int unreachable = Integer.MIN_VALUE;
    private double[][] Q;
    //对应节点到其他节点的可能性
    private double[][] Q_table;
    private int startNode = 4;
    private int endNode = 0;

    public void test() {
        for (int i = 0; i < network_rewardOfLinks.length; i++) {
            for (int j = 0; j < network_rewardOfLinks[i].length; j++) {
                System.out.print(network_rewardOfLinks[i][j]);
                System.out.print("    ");
            }
            System.out.println();
        }


    }

    // 获取起点和终点的序号
    public void initStart_And_End_Index() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 2) {
                    startIndex = i * map[0].length + j;
                } else if (map[i][j] == 3) {
                    endIndex = i * map[0].length + j;
                }
            }
        }
    }


    // 初始化Q表
    public void initQ() {
        // 对应四种运动可能 上下左右
        Q = new double[map.length * map[0].length][4];
        for (int i = 0; i < Q.length; i++) {
            for (int j = 0; j < Q[i].length; j++) {
                Q[i][j] = 0.001;
            }
        }
    }

    //网络结构，例如
    //1 6 (0节点连接1和6节点)
    //0 2
    public void initNetwork() {
//        for(int i=0;i<network_node;i++){
//            for(int j=0;j<network_node;j++)
//            {
//                network_full[i][j]=Integer.MAX_VALUE;
//            }
//        }
//        for(int i=0;i<network_link.length;i++){
//            int node1=network_link[i][0];
//            int node2=network_link[i][1];
//            network_full[node1][node2]=1;
//            network_full[node2][node1]=1;
//        }
//        //建立network_reachable_node
//        for(int i=0;i<network_node;i++){
//            int node_reachable=0;
//            for(int j=0;j<network_node;j++){
//                if(network_full[i][j]!=Integer.MAX_VALUE){
//                    node_reachable++;
//                }
//            }
//            int k=0;
//            network_reachable_node[i]=new int[node_reachable];
//            for(int j=0;j<network_full[i].length;j++){
//                if(network_full[i][j]!=Integer.MAX_VALUE){
//                    network_reachable_node[i][k++]=j;
//                }
//            }
//        }
//        //建立network_rewardOfLinks
//        //初始的时候全是-1，随着网络寻路的进行，有些节点会变成MIN_VALE
//        for(int i=0;i<network_node;i++){
//            network_rewardOfLinks[i]=new double[network_reachable_node[i].length];
//            for(int j=0;j<network_reachable_node[i].length;j++){
//                network_rewardOfLinks[i][j]=-1;
//            }
//        }
        Read readNetwork = new Read();
        ArrayList<ArrayList<Integer>> raw_network = readNetwork.readnetworkfile();
        network_node = raw_network.size();
        network_full=new int[network_node][network_node];
        int[] temp = new int[network_node];
        for(int i=0;i<network_node;i++){
            for(int j=0;j<network_node;j++){
                network_full[i][j]=Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < network_node; i++) {
//            System.out.println("i" + i);
            for (int j = 0; j < raw_network.get(i).size(); j++) {
//                System.out.println(raw_network.get(i).get(j) + "get");
                network_full[i][raw_network.get(i).get(j)] = 1;
                network_full[raw_network.get(i).get(j)][i] = 1;
            }
        }
        //建立network_reachable_node
        network_reachable_node=new int[network_node][];
        for(int i=0;i<network_node;i++){
            int node_reachable=0;
            for(int j=0;j<network_node;j++){
                if(network_full[i][j]!=Integer.MAX_VALUE){
                    node_reachable++;
                }
            }
            int k=0;
            network_reachable_node[i]=new int[node_reachable];
            for(int j=0;j<network_full[i].length;j++){
                if(network_full[i][j]!=Integer.MAX_VALUE){
                    network_reachable_node[i][k++]=j;
                }
            }
        }
        //建立network_rewardOfLinks
        //初始的时候全是-1，随着网络寻路的进行，有些节点会变成MIN_VALE
        double[] rewardsOfNodes=readNetwork.readRewardFromRewardfile(network_node);
        float[][] distance=readNetwork.readdistanceFromRewardfile(network_node);
        network_rewardOfLinks=new double[network_node][];

        for(int i=0;i<network_node;i++){
            network_rewardOfLinks[i]=new double[network_reachable_node[i].length];
            for(int j=0;j<network_reachable_node[i].length;j++){
                network_rewardOfLinks[i][j]=-(rewardsOfNodes[i]+rewardsOfNodes[j])/2;
                network_rewardOfLinks[i][j]-=distance[i][j]/10;
            }
        }
//        test();


    }

    //对应跟该节点到的其他节点的可能性
    public void initQ_table() {

        Q_table=new double[network_node][];
        for (int i = 0; i < network_node; i++) {
            Q_table[i] = new double[network_reachable_node[i].length];
            for (int j = 0; j < Q_table[i].length; j++) {
                Q_table[i][j] = 0.001;
            }
        }
    }
}

