#include<iostream>
#include <fstream>
#include<istream>
#include<sstream>
#include <string>
#include<string.h>
#include<vector>
using namespace std;



void test02()
{
    ifstream ifs;
    ifs.open("path.txt", ios::in);

    if (!ifs.is_open())
    {
        cout << "文件打开失败" << endl;
        return;
    }
    
    vector<vector<int>> node_links;



    string line;

    while (getline(ifs, line))
    {
        int colon_pos = line.find(":");
        string node_str = line.substr(0, colon_pos);
        int node = atoi(node_str.c_str());
        vector<int> links_temp;
        node_links.push_back(links_temp);
        //cout << node << endl;
        if (colon_pos!=line.size()-1) {
            string str_after = line.substr(colon_pos + 1, line.size() + 1);
            istringstream ss(str_after);
            
            string link;
            while (ss >> link) {
                node_links[node].push_back(atoi(link.c_str()));
            }
            //String[] nodes = split[1].split("\\s+");
            //for (int i = 0; i < nodes.length; i++) {
            //    int a = Integer.parseInt(nodes[i]);
            //    node_links.get(node).add(a);
            //}
            

        }
    }

    int node = node_links.size();
    int** network_full = new int* [node];
    for (int i = 0; i < node; i++) {
        network_full[i] = new int[node];
        for (int j = 0; j < node; j++) {
            network_full[i][j] = -1;
        }
    }
    for (int i = 0; i < node; i++) {
        for (int j = 0; j < node_links[i].size(); j++) {
            network_full[i][node_links[i][j]] = 1;
            network_full[node_links[i][j]][i] = 1;
        }
    }
    for (int i = 0; i < node; i++) {
        for (int j = 0; j < node; j++) {
            cout << network_full[i][j]<<"   ";
        }
        cout << endl;
    }
    ofstream ofs;
    //3.指定打开方式
    ofs.open("node_gate.txt", ios::out);
    //4.写内容
    for (int i = 0; i < node; i++) {
        ofs << i << ":" << endl;
        int gate_n = 0;
        for (int j = 0; j < node; j++) {
            if (network_full[i][j] != -1) {
                ofs << j << "," << gate_n << endl;
                gate_n++;
            }

        }
    }

    //5.关闭文件
    ofs.close();


    for (int i = 0; i < node_links.size(); i++) {
        for (int j = 0; j < node_links[i].size(); j++) {
            //cout << node_links[i][j] << "  ";
            cout << "rte[" << i << "]." << "port++ <--> C <--> rte[" << node_links[i][j] << "].port++; ";
            cout << endl;
            //cout << "rte[" << node_links[i][j] << "]." << "port++ <--> C <--> rte[" << i << "].port++; ";
            //cout << endl;
        }
        
    }




}
int main() {

    test02();

    //system("pause");

    return 0;
}