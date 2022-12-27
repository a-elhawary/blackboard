import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Stroke implements Shape{
    private ArrayList<Node> stroke;
    private int brushSize;
    private Color color;
    private final int SMOOTHING_CONSTANT = 5;
	private int lastIndex = 0;

    public Stroke(){
        stroke = new ArrayList<Node>();
    }

    public void addNode(int x, int y){
        stroke.add(new Node(x,y));
        if(getNumberOfNodes() > 1){
            int count = 0;
            int numNodes = 2;
            while(count < SMOOTHING_CONSTANT){
                int i = 0;
                int len = getNumberOfNodes();
                while(i < (numNodes - 1)){
                    stroke.add(len - (1 + i) , getMidPoint(stroke.get(len - (1+i)) , stroke.get(len - (2+i))));
                    i++;
                }
                numNodes += (numNodes - 1); 
                count++;
            }
        }
    }

    private Node getMidPoint(Node one, Node two){
        int x = (one.x + two.x) / 2;
        int y = (one.y + two.y) / 2;
        return (new Node(x , y));
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

    public void render(Graphics g){
        int i = 0;
        g.setColor(color);
        while(i < getNumberOfNodes()){
            g.fillOval(getNodeX(i), getNodeY(i), brushSize, brushSize);
            i++;
        }
        lastIndex = i;
    }

    public boolean isDone(){
        return getNumberOfNodes() >= lastIndex;
    }

    public void setBrushSize(int brushSize){
        this.brushSize = brushSize;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
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
