package RL_Path;

import java.io.*;
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


public class Write {
    public static void main(String[] args) {
        ArrayList<NodePos>nodesPosReadFromFile=new ArrayList<>();
        try {

            String strFile = "src/main/java/RL_Path/node_pos.txt";
            StringBuffer strSb = new StringBuffer();
            InputStreamReader inStrR = new InputStreamReader(new FileInputStream(strFile), "UTF-8");
            // character streams
            BufferedReader br = new BufferedReader(inStrR);
            String line = br.readLine();
            while(line!=null){
                String[] xy=line.split(",");
                nodesPosReadFromFile.add(new NodePos(Float.parseFloat(xy[0]),Float.parseFloat(xy[1])));
                line = br.readLine();
            }

        } catch (IOException e) {

        } finally {

        }
        for(int i=0;i<nodesPosReadFromFile.size();i++){
            System.out.print(nodesPosReadFromFile.get(i).x+","+nodesPosReadFromFile.get(i).y);
            System.out.println();
        }

        int node = nodesPosReadFromFile.size();
        int node_sqrt=(new Double(Math.sqrt(node)).intValue());
        System.out.println("node_sqrt"+node_sqrt);
        float grid = 10;
        float range=grid*1.5f;

        NodePos[] nodesPos = new NodePos[nodesPosReadFromFile.size()];
        nodesPosReadFromFile.toArray(nodesPos);
        Random random = new Random();
        //每个节点到其他节点的最短路径中的最大路径
        float distance_min_max=0;
        for (int i = 0; i < node; i++) {
//            System.out.println(nodesPos[i].x+"   "+nodesPos[i].y);
            //i节点到其他节点的最短路径
            float distance_min=Integer.MAX_VALUE;
            for(int j=0;j<node;j++){
                if(i!=j){
                    if(NodePos.distance(nodesPos[i],nodesPos[j])<distance_min)distance_min=NodePos.distance(nodesPos[i],nodesPos[j]);
                }
            }
            if(distance_min>distance_min_max)distance_min_max=distance_min;

        }

        //每个节点跟别的节点的连接
        ArrayList<Integer> links[] = new ArrayList[node];
        for (int i = 0; i < node; i++) {
            links[i] = new ArrayList<>();
            for(int j=0;j<node;j++){
                if(j>i){
                    if(NodePos.distance(nodesPos[i],nodesPos[j])<range){
                        links[i].add(j);
                    }
                }
            }
        }
        try {
            File file = new File("src/main/java/RL_Path/path.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(file);
            for (int i = 0; i < node; i++) {
                fileWritter.write(i + ":");
                for (int j = 0; j < links[i].size(); j++) {
                    if (links[i].get(j) > i) {
                        fileWritter.write(links[i].get(j).toString());
                        fileWritter.write("   ");
                        System.out.print(links[i].get(j).toString());
                        System.out.print("   ");
                    }
                }
                System.out.println("-----------");
                fileWritter.write("\n");
            }
            fileWritter.close();
            System.out.println("finish");
        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
