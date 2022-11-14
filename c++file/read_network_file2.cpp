#include<iostream>
#include <fstream>
#include<string>
using namespace std;
int main() {
    ifstream ifs;

    //3�����ļ������ж��Ƿ�򿪳ɹ�
    ifs.open("node_gate.txt", ios::in);
    if (!ifs.is_open()) {
        cout << "�ļ���ʧ��" << endl;
    }


    int node = 9;
    int** network_full = new int* [node];
    for (int i = 0; i < node; i++) {
        network_full[i] = new int[node];
        for (int j = 0; j < node; j++) {
            network_full[i][j] = -1;
        }
    }
    //������
    string buf;
    int from = -1;
    int to = -1;
    while (getline(ifs, buf)) {
        int colon_pos=buf.find(":");
        if (colon_pos != -1) {
            from = atoi(buf.substr(0, colon_pos).c_str());
            cout << "node" << from << endl;
        }
        else {
            int comma_pos = buf.find(",");
            to = atoi(buf.substr(0, comma_pos).c_str());
            cout << "to" << to << endl;
            int gate=atoi(buf.substr(comma_pos+1).c_str());
            cout << "gate" << gate << endl;
            network_full[from][to] = gate;
        }
        
        cout << buf << endl;
    }
    for (int i = 0; i < node; i++) {
        for (int j = 0; j < node; j++) {
            cout << network_full[i][j] << "   ";
        }
        cout << endl;
    }


    //5���ر��ļ�
    ifs.close();
    return 0;
}