package RL_Path;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
**  Create by: WSKH0929
    Date:2021-11-10
    Time:21:09
*/
public class Application extends javafx.application.Application {
    // 超出边界
    public double beyondBoundaryReward = -1000000;
    // 撞墙
    public double hitWallReward = -1000000;
    // 到了已经走过的节点
    public double reachAgainNode = -1000000;
    // 普通走路
    public double commonReward = -1;
    // 为了让每个方向都有一定概率
    public double minPossibility=0.01;
    // 到达终点
    public double reachEndReward = 10000;
    // 远离终点
    public double awayEndReward = -0.0001;
    // 奖励获取率
    public double alpha = 0.6;
    // reward衰减率 怕死系数 越接近1越怕死
    public double gamma_pasi = 0.00001;
    // reward衰减率 贪心系数 越接近1越贪心
    public double gamma_tanxin = 0.00001;
    // 设置邻域探索次数
    public int max_N = 50;
    // 学习迭代次数 999999 999999
    public int T = 5;
    //
    public int bestPathLen = Integer.MAX_VALUE;
    // 记录最佳路线
    List<Integer[]> bestPathList = new ArrayList<>();

    // 保存每次迭代的路线
    List<Integer[]> pathList = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
//        AnchorPane pane = new AnchorPane();
        Instance instance = new Instance();
        instance.initQ();
        instance.initNetwork(); // 初始化Q表
        instance.initQ_table();
        instance.test();
        instance.initStart_And_End_Index(); // 初始化起点终点索引
//        Canvas canvas = initCanvas(instance.getMap());
//        pane.getChildren().add(canvas);
        // 开始学习
        learn(instance);
//        primaryStage.setTitle("强化学习路径规划");
//        primaryStage.setScene(new Scene(pane, 600, 600, Color.YELLOW));
        primaryStage.show();
    }

    // 绘制初始地图
    public Canvas initCanvas(int[][] map) {
        Canvas canvas = new Canvas(400, 400);
        canvas.relocate(100, 100);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int m = map[i][j];
                GraphicsContext gc = canvas.getGraphicsContext2D();
                if (m == 0) {
                    gc.setFill(Color.GREEN);
                } else if (m == 1) {
                    gc.setFill(Color.BLACK);
                } else if (m == 2) {
                    gc.setFill(Color.PINK);
                } else if (m == 3) {
                    gc.setFill(Color.AQUA);
                }
                gc.fillRect(j * 20, i * 20, 20, 20);
            }
        }
        return canvas;
    }


    public void learn(Instance instance) {

        // 获取map
//        int[][] map = co(instance.getMap());
        // 获取网络结构

        int[][] network_reachable_node = instance.getNetwork_reachable_node();
        // 获取网络节点间奖励值
        double[][] network_rewardOfLinks = instance.getNetwork_rewardOfLinks();
        // 获取Q表
        double[][] Q_table = instance.getQ_table();
        for (int i = 0; i < T; i++) {
//            System.out.println(i);
            // 设置当前点所在位置 即起点
            int pos = instance.getStartNode();
//            System.out.println("起点是： "+pos);
            // 循环探索移动
            int n = 0;
            double[][] clone_network_rewardOfLinks = copyNetworkRewardOfLinks(network_rewardOfLinks);
            pathList = new ArrayList<>();

            int flag = 0;
            while (pos >= 0 && pos != Integer.MAX_VALUE && n < max_N) {
                double[] Qs = Q_table[pos];

//                if (flag < 1 && i < 1) {
//                    for (int t = 0; t < Qs.length; t++) {
//                        System.out.print("Qs[t]" + Qs[t]);
//                    }
//                    System.out.println("====================");
//                }

                //下一步怎么走，0 1 2 ...
                int next = next(Qs);


//                System.out.println("next"+next);
                double tempPos = move(pos, next, network_reachable_node, clone_network_rewardOfLinks, instance.getEndNode());



                // 找到Q最大的值
                double[] clone = Qs.clone();
                Arrays.sort(clone);
                double max_q = clone[clone.length - 1];
                // 更改Q表
                //到达终点
//                System.out.println("tempPos"+tempPos);
                if (tempPos == Integer.MAX_VALUE) {
//                    if (true) {
//                        System.out.println("迭代数:" + i + ",PathLen = " + (n + 1));
//                    }
//                    Q[pos][next-1] += ((1-alpha)*Q[pos][next-1]+alpha*(reachEndReward+gamma*max_q));
                    Q_table[pos][next] += reachEndReward;
                    pathList.add(new Integer[]{pos, next});
                    // 按照路线更新路线Q值 衰减率为gamma
                    if (n + 1 < bestPathLen || false) {
                        bestPathList = new ArrayList<>(pathList);
                        bestPathLen = n + 1;
//                        System.out.println("迭代数:"+i+",bestPathLen"+bestPathLen);
                        double reward = reachEndReward * gamma_tanxin / (double) (n + 1);
                        for (int k = pathList.size() - 1; k >= 0; k--) {
                            Integer[] integers = pathList.get(k);
                            Q_table[integers[0]][integers[1]] += reward;
                            reward = reachEndReward * gamma_tanxin;
                        }
                    }

                }
                //到达某个不是终点的点
                else if (tempPos >= 0) {
                    // next!=4&&next!=1

//                    Q[pos][next-1] += ((1-alpha)*Q[pos][next-1]+alpha*(commonReward+gamma*max_q));
                    Q_table[pos][next] += commonReward;

                    pathList.add(new Integer[]{pos, next});
                }
                //到达不可达点（已经走过的点）
                else {
//                    Q[pos][next-1] += ((1-alpha)*Q[pos][next-1]+alpha*(tempPos+gamma*max_q));
                    Q_table[pos][next] += tempPos;
                    // 按照路线更新路线Q值 衰减率为gamma
                    double reward = tempPos * gamma_pasi;
                    for (int k = pathList.size() - 1; k >= 0; k--) {
                        Integer[] integers = pathList.get(k);
                        Q_table[integers[0]][integers[1]] += reward;
                        reward = tempPos * gamma_pasi;
                    }
                }
                pos = (int) tempPos;
//                System.out.println(pos);
                flag++;
                n++;
            }
        }
        // 学习完毕 绘图 验证
//        verification_Q(canvas,Q,instance);
        // 绘制最佳路线
//        plotBestPath(canvas, instance);
        System.out.println("aaaaaaaaaaaaaaaaa");
        outputTextBestPath(instance,network_reachable_node);
        System.out.println("bbbbbbbbbbbbbbbbb");
    }

    // 传入四个数，按照概率返回 上下左右--->1234
    public int next(double[] Qs) {
        // 累积概率数组
        double[] accumulateRateArr = new double[Qs.length];
        // 概率数组
        double[] rateArr = new double[Qs.length];
        // 获取最小值
        double[] arr = Qs.clone();
        Arrays.sort(arr);
        double min = arr[0];
        // 调整四个数 +1 是防止数值为0的情况 尽可能让所有方向都有一定概率试探
//        n1 = n1 - min + Math.abs(commonReward);
//        n2 = n2 - min + Math.abs(commonReward);
//        n3 = n3 - min + Math.abs(commonReward);
//        n4 = n4 - min + Math.abs(commonReward);
        //这里减去min是为了让所有的数字都是正数
        for(int i=0;i<Qs.length;i++){
            Qs[i]=Qs[i]-min+Math.abs(minPossibility);
        }
        // 根据四个数计算概率
        double sum = 0;
        for (int i = 0; i < Qs.length; i++) {
            sum += Qs[i];
        }
        for (int i = 0; i < Qs.length; i++) {
            rateArr[i] = Qs[i] / sum;
        }

        // 计算累积概率
        for (int i = 0; i < accumulateRateArr.length; i++) {
            for (int j = 0; j <= i; j++) {
                accumulateRateArr[i] += rateArr[j];
//                System.out.println("accum"+i+"   "+accumulateRateArr[i]);
            }
        }


        //        accumulateRateArr[0] = rateArr[0];
//        accumulateRateArr[1] = rateArr[0] + rateArr[1];
//        accumulateRateArr[2] = rateArr[0] + rateArr[1] + rateArr[2];
//        accumulateRateArr[3] = rateArr[0] + rateArr[1] + rateArr[2] + rateArr[3];
        // 轮盘赌
        double r = new Random().nextDouble();
//        System.out.println("r = "+r+","+Arrays.toString(accumulateRateArr));

        // 根据轮盘赌随机数选择方向
        for (int i = 0; i < accumulateRateArr.length; i++) {
            if (r < accumulateRateArr[i]) return i;
        }
        return -1;
    }


    // 移动指令 获取移动后的坐标 如果移动返回为负数 则为惩罚 否则则说明成功移动
    // 到终点则返回 Integer.MAX_VALUE
    public double move(int pos, int next, int[][] network_reachable_node, double[][] network_rewardOfLinks,
                       int endNode) {

        //如果下一个节点是一个已经被走过的节点
        if (network_rewardOfLinks[pos][next] == Integer.MIN_VALUE) {
            return reachAgainNode;
        }
        //如果下一个节点还没有被走过
        else {
            int next_pos = network_reachable_node[pos][next];
            //如果到达终点，返回最大值
            if (next_pos == endNode) return Integer.MAX_VALUE;
            //如果没到达终点，返回下一个点的位置；并且，把该点位置置为MIN_VALUE
            else {
                network_rewardOfLinks[pos][next]=Integer.MIN_VALUE;
                return network_reachable_node[pos][next];
            }
        }
    }

    // 绘制最佳路线
    public void plotBestPath(Canvas canvas, Instance instance) {

        int[][] map = instance.getMap();
        System.out.println("起点为：" + instance.getStartIndex());
        for (Integer[] integers : bestPathList) {
            System.out.print(Arrays.toString(integers));
        }
        System.out.println();
        for (int i = 0; i < bestPathList.size(); i++) {
            int pos = bestPathList.get(i)[0];
            int colLen = map[0].length;
            int y = pos % colLen;
            int x = (pos - y) / colLen;

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.GRAY);
            gc.fillRect(y * 20, x * 20, 20, 20);

            // 绘制文字
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("微软雅黑", 15));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.TOP);
            gc.fillText("" + (i + 1), y * 20 + 10, x * 20);
        }

        System.out.println("最少步数为:" + bestPathLen);
    }

    // 绘制最佳路线
    public void outputTextBestPath(Instance instance,int[][] network_reachable_node) {


        System.out.println("b");
//        System.out.println("起点为：" + instance.getStartIndex());
        for (Integer[] integers : bestPathList) {
            System.out.print("["+integers[0]+","+ network_reachable_node[integers[0]][integers[1]]+"]");
        }
//        System.out.println();
        for (int i = 0; i < bestPathList.size(); i++) {
            int pos = bestPathList.get(i)[0];
            System.out.println("finalpos"+pos);

        }

//        System.out.println("最少步数为:" + bestPathLen);
    }

    // 按照Q表进行移动 每次选择Q值最大的移动方向 并且绘图
    public void verification_Q(Canvas canvas, double[][] Q, Instance instance) {
        System.out.println("最终Q表为：");
        for (double[] q : Q) {
            System.out.println(Arrays.toString(q));
        }
        System.out.println("地图为：");
        int pos = instance.getStartIndex();
        int[][] map = instance.getMap();
        for (int i = 0; i < map.length; i++) {
            System.out.println(Arrays.toString(map[i]));
        }
        int n = 0;
        while (pos >= 0 && pos != Integer.MAX_VALUE && n < 500) {

            int colLen = map[0].length;
            int j = pos % colLen;
            int i = (pos - j) / colLen;

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.GRAY);
            gc.fillRect(j * 20, i * 20, 20, 20);

            // 绘制文字
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("微软雅黑", 15));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.TOP);
            gc.fillText("" + (n + 1), j * 20 + 10, i * 20);

            pos = moveToMax(pos, Q[pos].clone(), map);

            n++;

        }
        System.out.println("步数为:" + n);
    }

    // 向最小化惩罚的方向移动
    public int moveToMax(int pos, double[] Qs, int[][] map) {
        int colLen = map[0].length;
        // 首先将pos转化为二维坐标
        int j = pos % colLen;
        int i = (pos - j) / colLen;
        int tempI = 0, tempJ = 0;
        // 获取最大化奖励的方向
        int maxIndex = -1;
        double maxValue = -1 * Double.MAX_VALUE;
        for (int k = 0; k < Qs.length; k++) {
            // &&map[i][j]!=1
            if (Qs[k] > maxValue && map[i][j] != 1) {
                maxValue = Qs[k];
                maxIndex = k;
            }
        }
        // 移动坐标
        switch (maxIndex + 1) {
            case 1:
                // 上移
                tempI = i - 1;
                tempJ = j;
                break;
            case 2:
                // 下移
                tempI = i + 1;
                tempJ = j;
                break;
            case 3:
                // 左移
                tempI = i;
                tempJ = j - 1;
                break;
            case 4:
                // 右移
                tempI = i;
                tempJ = j + 1;
                break;
            default:
        }
        System.out.println(pos + "  " + Arrays.toString(Qs) + "  maxIndex+1 = " + (maxIndex + 1));
        if (tempI >= map.length || tempJ >= map[0].length || tempI < 0 || tempJ < 0) {
            // 超出边界
            return -100;
        } else if (map[tempI][tempJ] == 1) {
            // 撞墙
            return -100;
        } else if (map[tempI][tempJ] == 3) {
            // 到终点了
            return Integer.MAX_VALUE;
        }
        //
        map[i][j] = 1;
        return (tempI * colLen + tempJ);
    }

    // 复制map
    public double[][] copyNetworkRewardOfLinks(double[][] NetworkRewardOfLinks) {
        double[][] tempNetwork = new double[NetworkRewardOfLinks.length][];
        for (int i = 0; i < tempNetwork.length; i++) {
            tempNetwork[i] = NetworkRewardOfLinks[i].clone();
        }
        return tempNetwork;
    }


    public static void main(String[] args) {
        launch(args);
    }

}