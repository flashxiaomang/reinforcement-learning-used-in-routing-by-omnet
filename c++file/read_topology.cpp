#include <fstream>
#include<sstream>
#include <string>
#include <iostream>
#include<vector>
using namespace std;
void test01()
{
	ifstream ifs;
	ifs.open("test.txt", ios::in);

	if (!ifs.is_open())
	{
		cout << "�ļ���ʧ��" << endl;
		return;
	}

	//��һ�ַ�ʽ
	//char buf[1024] = { 0 };
	//while (ifs >> buf)
	//{
	//	cout << buf << endl;
	//}

	//�ڶ���
	//char buf[1024] = { 0 };
	//while (ifs.getline(buf,sizeof(buf)))
	//{
	//	cout << buf << endl;
	//}

	//������
	string buf;
	int node = 9;
	vector<int>* link_nodes = new vector<int>[node * (node - 1)];
	int i = 0;
	while (getline(ifs, buf))
	{
		//cout << buf << endl;
		//int a=buf.find("-");
		//string source = buf.substr(0, a);
		//cout << source << endl;
		int b = buf.find(":");
		//string destination = buf.substr(a + 1, b - (a+1));
		//cout << destination << endl;
		string stra = buf.substr(b + 1);
		istringstream ss(stra);
		vector<string> words;
		string word;
		while (ss >> word) {
			words.push_back(word);
		}
		if (i == 64) {
			int aa = 0;
		}
		for (string x : words) {
			link_nodes[i].push_back(atoi(x.c_str()));
		}
		i++;
	}
	int flag = 0;
	//for (int i = 0; i < node; i++) {
	//	for (int j = 0; j < node - 1; j++) {
	//		if (j == 7) {
	//			int a = 0;
	//		}
	//		cout << i << "---";
	//		cout << (j < i ? j : j + 1);
	//		cout << ":";
	//		flag++;
	//		int abc = i * (node - 1) + j;
	//		for (int k = 0; k < link_nodes[i * (node - 1) + j].size(); k++) {

	//			cout << link_nodes[i * (node - 1) + j][k];
	//			cout << "-";
	//		}
	//		cout << endl;
	//	}
	//}
	i = 6;
	int j_ = 0;//dest
	//link_nodes�е�[3,3]��[source=3,dest=4]��link_nodes�е�[3,4]��[source=3,dest=5]��Ҳ������һ����λ
	int j = j_ < i ? j_ : j_ - 1;//ת����link_nodes�еĵڶ���ά��
	//for (int k = 0; k < link_nodes[i * (node - 1) + j].size(); k++) {
	//	cout << link_nodes[i * (node - 1) + j][k];
	//	cout << "   ";
	//}
	int hopCount = 1;
	cout << link_nodes[i * (node - 1) + j][hopCount];


	//������
	//char c;
	//while ((c = ifs.get()) != EOF)
	//{
	//	cout << c;
	//}

	//ifs.close();


}

int main() {

	test01();

	system("pause");

	return 0;
}