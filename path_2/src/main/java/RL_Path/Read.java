package RL_Path;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Read {
    public ArrayList<ArrayList<Integer>> readnetworkfile() {
        ArrayList<ArrayList<Integer>> node_links = new ArrayList<>();
        try {

            String strFile = "src/main/java/RL_Path/path.txt";
            StringBuffer strSb = new StringBuffer();
            InputStreamReader inStrR = new InputStreamReader(new FileInputStream(strFile), "UTF-8");
            // character streams
            BufferedReader br = new BufferedReader(inStrR);
            String line = br.readLine();
            while (line != null) {
//                System.out.println(line);
                String[] split = line.split(":");
//                System.out.println(split[0]);
                int node = Integer.parseInt(split[0]);
                node_links.add(new ArrayList<>());
                if (split.length > 1) {
                    String[] nodes = split[1].split("\\s+");
                    for (int i = 0; i < nodes.length; i++) {
                        int a = Integer.parseInt(nodes[i]);
                        node_links.get(node).add(a);
                    }
                }
                strSb.append(line).append("\n");
                line = br.readLine();
            }
//            System.out.println(node_links.size() + "size");
            for (int i = 0; i < node_links.size(); i++) {
                for (int j = 0; j < node_links.get(i).size(); j++) {
//                    System.out.print(node_links.get(i).get(j));
//                    System.out.print("  ");
                }
//                System.out.println("-----");
            }

        } catch (IOException e) {

        } finally {

        }
        return node_links;
    }
    public double[] readRewardFromRewardfile(int network_node){
        //各个节点的奖励值（总的）
        double[] rewards = new double[network_node];
        //各个节点的奖励值是根据以下三个参数算出
        double[] delay_mean=new double[network_node];
        double[] drop_sum=new double[network_node];
        double[] delay_shake=new double[network_node];
        double[] distance=new double[network_node];
        try {

            String strFile = "src/main/java/RL_Path/node_reward.txt";
            StringBuffer strSb = new StringBuffer();
            InputStreamReader inStrR = new InputStreamReader(new FileInputStream(strFile), "UTF-8");
            // character streams
            BufferedReader br = new BufferedReader(inStrR);
            String line = br.readLine();
            //当前读到了第几行，行数从0开始
            int line_i=0;
            while (line != null) {
//                System.out.println(line);
                //如果不是每段开头的那行
                if(line_i%(network_node+1)!=0){
                    String[] split = line.split(":");
                    //当前读到了第i个节点
                    int node_i=line_i%(network_node+1)-1;
                    switch (line_i/(network_node+1)){
                        case 0:delay_mean[node_i]=Double.parseDouble(split[1]);
                        case 1:drop_sum[node_i]=Double.parseDouble(split[1]);
                        case 2:delay_shake[node_i]=Double.parseDouble(split[1]);
                    }

                }

                line=br.readLine();
                line_i++;

            }
            for(int i=0;i<network_node;i++){
//                System.out.println(delay_mean[i]);
//                System.out.println(drop_sum[i]/100000000);
//                System.out.println(delay_shake[i]/3);
                rewards[i]=delay_mean[i]+drop_sum[i]/100000000+delay_shake[i]/3;
            }


        } catch (IOException e) {

        } finally {

        }

        return rewards;
    }
    public float[][] readdistanceFromRewardfile(int network_node){
        NodePos[] nodesPos=new NodePos[network_node];
        float[][] distance=new float[network_node][network_node];
        try {

            String strFile = "src/main/java/RL_Path/node_pos.txt";
            StringBuffer strSb = new StringBuffer();
            InputStreamReader inStrR = new InputStreamReader(new FileInputStream(strFile), "UTF-8");
            // character streams
            BufferedReader br = new BufferedReader(inStrR);
            String line = br.readLine();
            int node_i=0;
            //当前读到了第几行，行数从0开始
            while (line != null) {
                String[] poses=line.split(",");
                nodesPos[node_i]=new NodePos(Float.parseFloat(poses[0]),Float.parseFloat(poses[1]));
                line=br.readLine();
                node_i++;
            }
            for(int i=0;i<network_node;i++){
                for(int j=0;j<network_node;j++){
                    distance[i][j]=NodePos.distance(nodesPos[i],nodesPos[j]);
                    distance[j][i]=NodePos.distance(nodesPos[i],nodesPos[j]);
                }
            }


        } catch (IOException e) {

        } finally {

        }
        return distance;
    }

}


