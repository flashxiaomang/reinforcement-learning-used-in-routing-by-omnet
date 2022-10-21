//package RL_Path;
//
//public class WriteOld {
//}
//package RL_Path;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Random;
//
///*
//网络的生成规则如下：
//参数node, least_link_num, most_link_num
//生成node个节点，每个节点生成一些链路，链路个数为least_link_num到most_link_num
//由于该网络是无向图，所以如果节点i到节点j有链路，那么节点j到节点i也有链路。
//这样的话会有重复生成的链路：比如节点i生成了到节点j的链路，节点j也生成了到节点i的链路。重复生成的链路只算一个链路。
// */
//public class Write {
//    public static void main(String[] args) {
//        //网络中的节点数
//        int node = 10;
//        double least_link_parameter = 0.1;
//        double most_link_parameter = 0.3;
//        int least_link_num = (int) (node * least_link_parameter);
//        int most_link_num = (int) (node * most_link_parameter);
//        ArrayList<Integer> links[] = new ArrayList[node];
//        for (int i = 0; i < node; i++) {
//            links[i] = new ArrayList<>();
//            Random random = new Random();
//            //第i个节点需要的链路数是least_link_num和most_link_num之间的一个随机数
//            int linkNumOfLinkI = random.nextInt(most_link_num - least_link_num + 1) + least_link_num;
//            //已经生成的链路数
//            int linkGeneratored = 0;
//            while (linkGeneratored < linkNumOfLinkI) {
//                int tempNodeReached = random.nextInt(node);
//                if (!links[i].contains(tempNodeReached) && i != tempNodeReached) {
//                    links[i].add(tempNodeReached);
//                    linkGeneratored++;
//                }
//            }
//            Collections.sort(links[i]);
//        }
//
//
////        ArrayList<Integer> inttest=new ArrayList<>();
//        try {
//
//            File file = new File("src/main/java/RL_Path/path.txt");
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//            FileWriter fileWritter = new FileWriter(file);
//            for (int i = 0; i < node; i++) {
//                fileWritter.write(i + ":");
//                for (int j = 0; j < links[i].size(); j++) {
//                    if (links[i].get(j) > i) {
//                        fileWritter.write(links[i].get(j).toString());
//                        fileWritter.write("   ");
//                        System.out.print(links[i].get(j).toString());
//                        System.out.print("   ");
//                    }
//                }
//                System.out.println("-----------");
//                fileWritter.write("\n");
//            }
//            fileWritter.close();
//            System.out.println("finish");
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        }
//
//        for (int i = 0; i < node; i++) {
//
//        }
//    }
//}
