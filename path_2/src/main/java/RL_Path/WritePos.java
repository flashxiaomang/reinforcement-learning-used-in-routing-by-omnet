package RL_Path;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math.*;

/*
网络的生成规则如下：
参数node, least_link_num, most_link_num
生成node个节点，每个节点生成一些链路，链路个数为least_link_num到most_link_num
由于该网络是无向图，所以如果节点i到节点j有链路，那么节点j到节点i也有链路。
这样的话会有重复生成的链路：比如节点i生成了到节点j的链路，节点j也生成了到节点i的链路。重复生成的链路只算一个链路。
 */


public class WritePos {
    public static void main(String[] args) {
        int node_sqrt = 3;

        int node = node_sqrt * node_sqrt;
        float grid = 10;

        NodePos[] nodesPos = new NodePos[node];
        Random random = new Random();
        for (int i = 0; i < node_sqrt; i++) {
            for (int j = 0; j < node_sqrt; j++) {
                nodesPos[i * node_sqrt + j] = new NodePos(random.nextFloat() * grid+i*grid, random.nextFloat() * grid+j*grid);
            }
        }
        //每个节点到其他节点的最短路径中的最大路径
        float distance_min_max=0;
        for (int i = 0; i < node; i++) {
//            System.out.println(nodesPos[i].x+"   "+nodesPos[i].y);
            //i节点到其他节点的最短路径
            float distance_min=Integer.MAX_VALUE;
            for(int j=0;j<node;j++){
                if(i!=j){
                    if(NodePos.distance(nodesPos[i],nodesPos[j])<distance_min)distance_min= NodePos.distance(nodesPos[i],nodesPos[j]);
                }
            }
            if(distance_min>distance_min_max)distance_min_max=distance_min;

        }

        try {
//
            File file = new File("src/main/java/RL_Path/node_pos.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(file);
            for (int i = 0; i < nodesPos.length; i++) {
                fileWritter.write(String.valueOf(nodesPos[i].x));
                fileWritter.write(",");
                fileWritter.write(String.valueOf(nodesPos[i].y));
                fileWritter.write("\n");
            }
            fileWritter.close();
            System.out.println("finish");
        } catch (IOException e) {

            e.printStackTrace();

        }




    }
}
