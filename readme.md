操作流程：
注：标点号的是笔者完成的，读者无需操作
* 1，运行Java文件WritePos.java，生成网络节点（随机生成），写入node_pos.txt文件
* 2，运行Java文件Write.java，生成网络节点之间的链路（根据node_pos.txt来生成），写入path.txt文件
* 3，将path.txt文件复制到一个c++文件中（我没有上传），生成在omnet中需要写的东西，然后复制到omnet中
* 4，运行omnet，得到一个.anf文件（统计信息文件），导出为Untitled.csv文件，放到D:\Untitled.txt。
* 5，运行pathon文件main.py，读取D:\Untitled.txt，得到node_reward.txt文件。
* 6，将node_reward.txt文件放到java项目中。

7，运行Java文件Application.java，得到网络算出路由。

8，（未完成）把这个路由生成一个路由表，把该表放到omnet中，用它来路由。

说明：

1，那个文件夹是java文件夹，用IDEA打开，我用的是IDEA2018，java8或以上。

2，上述的操作流程中，1-2生成网络节点位置和节点间连接，3生成omnet中的网络定义代码，4是运行omnet，导出结果，5统计结果，形成文件，6通过该文件以及节点位置来定义网络链路的奖励（惩罚）值，7找到最佳路由。

3，网络中链路的奖励（惩罚值）是这样定义的：根据节点的时延、丢包、抖动，再加上节点间距离（一共4个参数），定义节点间的奖励（惩罚）值，也就是强化学习中的状态转移的reward。
