package RL_Path;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
//        int network_node;
//        int[][] network_full;
//        Read readNetwork = new Read();
//        ArrayList<ArrayList<Integer>> raw_network = readNetwork.readnetworkfile();
//        network_node = raw_network.size();
//        network_full = new int[network_node][network_node];
//        int[] temp = new int[network_node];
//        for(int i=0;i<network_node;i++){
//            for(int j=0;j<network_node;j++){
//                network_full[i][j]=Integer.MAX_VALUE;
//            }
//        }
//        for (int i = 0; i < network_node; i++) {
//            System.out.println("i" + i);
//            for (int j = 0; j < raw_network.get(i).size(); j++) {
//                System.out.println(raw_network.get(i).get(j) + "get");
//                network_full[i][raw_network.get(i).get(j)] = 1;
//                network_full[raw_network.get(i).get(j)][i] = 1;
//            }
//        }
//
//        for (int i = 0; i < network_full.length; i++) {
//            for (int j = 0; j < network_full[i].length; j++) {
//                System.out.print(network_full[i][j]);
//                System.out.print("    ");
//            }
//            System.out.println();
//        }


//        ArrayList<Integer> integers=new ArrayList<>();
//        integers.add(1);
//        integers.add(2);
//        Integer[] ints=new Integer[integers.size()];
//        integers.toArray(ints);
//        System.out.println(ints[0]);


        int network_node=9;
        //各个节点的奖励值（总的）
        double[] rewards = new double[network_node];
        //各个节点的奖励值是根据以下三个参数算出
        double[] delay_mean=new double[network_node];
        double[] drop_sum=new double[network_node];
        double[] delay_shake=new double[network_node];
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
                System.out.println(line_i);

            }
            for(int i=0;i<network_node;i++){
//                System.out.println(delay_mean[i]);
//                System.out.println(drop_sum[i]/100000000);
//                System.out.println(delay_shake[i]/3);
                rewards[i]=delay_mean[i]+drop_sum[i]/100000000+delay_shake[i]/3;
                System.out.println(rewards[i]);
            }


        } catch (IOException e) {

        } finally {

        }

    }

}
