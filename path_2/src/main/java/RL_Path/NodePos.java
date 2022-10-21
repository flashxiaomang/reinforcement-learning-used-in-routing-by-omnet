package RL_Path;

public class NodePos {
    float x;
    float y;

    NodePos(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public static float distance(NodePos node1, NodePos node2){
        double a=1;
        float b=2;
        double temp=(Math.sqrt((node1.x-node2.x)*(node1.x-node2.x)+(node1.y-node2.y)*(node1.y-node2.y)));
        return new Float(temp).floatValue();
    }
}