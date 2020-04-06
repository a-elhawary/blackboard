import java.util.ArrayList;
import java.awt.Graphics;

public class Stroke{
    private ArrayList<Node> stroke;
    private int brushSize;

    public Stroke(){
        stroke = new ArrayList<Node>();
    }

    public void addNode(int x, int y){
        stroke.add(new Node(x,y));
    }

    public int getNumberOfNodes(){
        return stroke.size();
    }

    public int getNodeX(int index){
        return stroke.get(index).x;
    }

    public int getNodeY(int index){
        return stroke.get(index).y;
    }

    public int renderStroke(Graphics g, int lastNode){
        int i = lastNode;
        while(i < getNumberOfNodes()){
            g.fillOval(getNodeX(i), getNodeY(i), brushSize, brushSize);
            i++;
        }
        return i;
    }

    public boolean isDone(int lastIndexRendered){
        return getNumberOfNodes() >= lastIndexRendered;
    }

    public void setBrushSize(int brushSize){
        this.brushSize = brushSize;
    }

    private class Node{
        public int x;
        public int y;
        public Node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}