生成一个网络，写入文件。然后读该文件，建立网络结构。
生成网络：例如有10个节点，每个节点有1-3个连接，那么随机生成这些连接。例如i到j有连接，那么j到i也自动生成连接。如果i到j生成了连接，j到i又生成连接，只算一次。
例如点1到点3有连接，在写入文件时，只写入1到3这个连接。
写入文件：每个点到哪些点有连接（例如9到5是有连接的，但在5：那一行已经记录了，所以在9：那一行不再记录
0:2   8
1:4   6
2:3   4   7
3:8
4:
5:9
6:
7:
8:
9:
读文件：
读入一个ArrayList<ArrayList<Integer>>变量当中，相当于一个二维数组，第一维是节点，第二维是该节点可达的节点。
建立网络：
例如3：8，3到8有连接，8到3也有连接。