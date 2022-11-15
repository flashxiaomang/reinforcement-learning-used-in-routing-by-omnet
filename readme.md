操作流程：
有三个文件夹：
1）c++file，这个文件夹存放所有的c++代码。你可以用visual studio新建一个项目，然后把我的c++代码复制到项目中。
2）path_2，这个文件夹存放所有的java代码
3）python_folder，这个文件夹存放所有的python代码。你可以用pycharm新建一个项目，然后把我的python代码复制到项目中。
4）omnet_folder，这个文件夹存放两个omnet项目，routing项目是用的默认路由，routing_another用的是我修改后的路由（也就是本文所讲的路由）
5）result_analysis_folder，这个文件夹存放运行结果信息，以及用python画图的代码。
注：标点号的是笔者完成的，读者无需操作
* 1，运行Java文件WritePos.java，生成网络节点（随机生成），写入node_pos.txt文件。具体：设置节点个数（某个整数的平方，比如9，16，25），代码会随机生成节点位置。
* 2，运行Java文件Write.java，生成网络节点之间的链路（根据node_pos.txt来生成），写入path.txt文件
* 3，将path.txt文件复制到一个c++文件中（read_network_file1.cpp），生成在omnet中需要写的东西，然后复制到omnet的network/myned.ned中。需要复制到omnet中的东西如下：
rte[0].port++ <--> C <--> rte[1].port++;
rte[0].port++ <--> C <--> rte[3].port++;
rte[1].port++ <--> C <--> rte[2].port++;
rte[1].port++ <--> C <--> rte[3].port++;
rte[1].port++ <--> C <--> rte[4].port++;
rte[1].port++ <--> C <--> rte[5].port++;
rte[1].port++ <--> C <--> rte[6].port++;
rte[2].port++ <--> C <--> rte[4].port++;
rte[2].port++ <--> C <--> rte[5].port++;
rte[2].port++ <--> C <--> rte[8].port++;
rte[3].port++ <--> C <--> rte[4].port++;
rte[3].port++ <--> C <--> rte[5].port++;
rte[3].port++ <--> C <--> rte[6].port++;
rte[3].port++ <--> C <--> rte[8].port++;
rte[4].port++ <--> C <--> rte[5].port++;
rte[4].port++ <--> C <--> rte[6].port++;
rte[4].port++ <--> C <--> rte[7].port++;
rte[4].port++ <--> C <--> rte[8].port++;
rte[5].port++ <--> C <--> rte[6].port++;
rte[5].port++ <--> C <--> rte[8].port++;
rte[6].port++ <--> C <--> rte[7].port++;
rte[7].port++ <--> C <--> rte[8].port++;
* 4，运行omnet，得到一个.anf文件（统计信息文件），导出为Untitled.csv文件，放到D:\Untitled.txt。这个文件是节点的统计信息。
* 5，运行pathon文件main.py，读取D:\Untitled.txt，得到node_reward.txt文件。这是从Untitled.txt文件中提取出的时延、抖动、丢包信息。
* 6，将node_reward.txt文件复制到java项目中。java项目会根据该文件（时延、抖动、丢包信息），再加上节点之间的距离，计算节点之间的reward值。
* 7，运行Java文件Application.java，得到网络算出路由，生成routing_nodes.txt文件。这个文件是路由信息文件。
* 8，把routing_nodes.txt文件复制到omnet项目（routing或者是routing_another）中，并重命名为test.txt。omnet会根据这个文件来路由（寻找节点之间的路径）。
* 9，运行c++文件read_netword_file，根据节点信息文件path.txt生成node_gate.txt文件，这个文件是记录节点间链路对应着哪个门，比如节点1的第2个门连接着节点4，那么node_gate.txt中的记录是：1-4: 2。
* 10，把node_gate.txt文件复制到omnet项目中，omnet项目会根据这个来寻找节点之间传输信息要走哪个门。
* 11，运行omnet项目（默认路由的routing项目或者非默认路由的routing_another项目），选择myned网络，得出result/myned.sca，点击这个，生成myned.anf，点击myned.anf，点击它的Scalars项，全选，右键导出为csv文件。
12，可以修改omnet.ini中的**.sendIaTime = uniform(100ms,500ms)的100和500那两个数字，也就是修改网络中消息发送的间隔。如下所示：
[Myned]
network = networks.Myned
#**.destAddresses = "1 28 50"
**.destAddresses = "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15"
#myadd
**.sendIaTime = uniform(100ms,500ms)# 原来是10 50
#myadd
**.frameCapacity = 3
根据不同的间隔，得出不同的统计数据（即myned.anf文件），根据它的scalars项，导出为csv文件。
* 13，运行result_analysis_folder中的main.py和main2.py，得出统计数据图。


说明：

1，那个文件夹是java文件夹，用IDEA打开，我用的是IDEA2018，java8或以上。

2，网络中链路的奖励（惩罚值）是这样定义的：根据节点的时延、丢包、抖动，再加上节点间距离（一共4个参数），定义节点间的奖励（惩罚）值，也就是强化学习中的状态转移的reward。
