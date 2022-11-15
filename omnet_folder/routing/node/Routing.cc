//
// This file is part of an OMNeT++/OMNEST simulation example.
//
// Copyright (C) 1992-2015 Andras Varga
//
// This file is distributed WITHOUT ANY WARRANTY. See the file
// `license' for details on this and other legal matters.
//

#ifdef _MSC_VER
#pragma warning(disable:4786)
#endif

#include <map>
#include <omnetpp.h>
#include "Packet_m.h"
#include <fstream>
#include<sstream>
#include <string>
#include <iostream>
#include<vector>
using namespace std;

using namespace omnetpp;

/**
 * Demonstrates static routing, utilizing the cTopology class.
 */
class Routing : public cSimpleModule
{
  private:
    int myAddress;

    typedef std::map<int, int> RoutingTable;  // destaddr -> gateindex
    RoutingTable rtable;
    vector<int>* linksOfNodes;

    simsignal_t dropSignal;
    simsignal_t outputIfSignal;

  protected:
    virtual void initialize() override;
    virtual void handleMessage(cMessage *msg) override;
    void readTopologyFile();
};

Define_Module(Routing);

void Routing::initialize()
{

    readTopologyFile();
    myAddress = getParentModule()->par("address");

    dropSignal = registerSignal("drop");
    outputIfSignal = registerSignal("outputIf");

    //
    // Brute force approach -- every node does topology discovery on its own,
    // and finds routes to all other nodes independently, at the beginning
    // of the simulation. This could be improved: (1) central routing database,
    // (2) on-demand route calculation
    //
    cTopology *topo = new cTopology("topo");

    std::vector<std::string> nedTypes;
    nedTypes.push_back(getParentModule()->getNedTypeName());
    topo->extractByNedTypeName(nedTypes);
    EV << "cTopology found " << topo->getNumNodes() << " nodes\n";

    cout<<"test the to"<<getParentModule();
    EV<<"test the to"<<getParentModule()->getIndex();
    cTopology::Node *thisNode = topo->getNodeFor(getParentModule());

    // find and store next hops
    for (int i = 0; i < topo->getNumNodes(); i++) {
        if (topo->getNode(i) == thisNode)
            continue;  // skip ourselves
        topo->calculateUnweightedSingleShortestPathsTo(topo->getNode(i));

        if (thisNode->getNumPaths() == 0)
            continue;  // not connected

        cGate *parentModuleGate = thisNode->getPath(0)->getLocalGate();
        int gateIndex = parentModuleGate->getIndex();
        int address = topo->getNode(i)->getModule()->par("address");
        rtable[address] = gateIndex;
        EV<<"thisIndex"<<getParentModule()->getIndex()<<endl;
        cout<<"thisIndex"<<getParentModule()->getIndex()<<endl;
        cout<<"address"<<address<<endl;
        cout<<"thisNodeGetPath0"<<thisNode->getPath(0)->getLocalGate()->getIndex()<<endl;
        EV << "  towards address " << address << " gateIndex is " << gateIndex << endl;
    }
    delete topo;
}

void Routing::readTopologyFile(){
    ifstream ifs;
    ifs.open("test.txt", ios::in);

    if (!ifs.is_open())
    {
        cout << "文件打开失败" << endl;
        return;
    }

    //第一种方式
    //char buf[1024] = { 0 };
    //while (ifs >> buf)
    //{
    //  cout << buf << endl;
    //}

    //第二种
    //char buf[1024] = { 0 };
    //while (ifs.getline(buf,sizeof(buf)))
    //{
    //  cout << buf << endl;
    //}

    //第三种
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
        for (string x : words) {
            link_nodes[i].push_back(atoi(x.c_str()));
        }
        i++;
    }

    linksOfNodes=link_nodes;
    EV<<"get topology success";
}
void Routing::handleMessage(cMessage *msg)
{
    Packet *pk = check_and_cast<Packet *>(msg);
    int destAddr = pk->getDestAddr();
    //myadd
    EV << "routing handle message" << endl;

    if (destAddr == myAddress) {
        EV << "local delivery of packet " << pk->getName() << endl;
        send(pk, "localOut");
        emit(outputIfSignal, -1);  // -1: local
        return;
    }

    RoutingTable::iterator it = rtable.find(destAddr);
    if (it == rtable.end()) {
        EV << "address " << destAddr << " unreachable, discarding packet " << pk->getName() << endl;
        emit(dropSignal, (intval_t)pk->getByteLength());
        delete pk;
        return;
    }

    int outGateIndex = (*it).second;
    EV << "forwarding packet " << pk->getName() << " on gate index " << outGateIndex << endl;
    pk->setHopCount(pk->getHopCount()+1);
    emit(outputIfSignal, outGateIndex);

    send(pk, "out", outGateIndex);
}



