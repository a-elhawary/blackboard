import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Stack;

public class DrawingPanel extends JPanel{

    private Color background;
    private Color foreground;

    private int width;
    private int height;
    private int brushSize = 8;
    private boolean clearScreen;
    private boolean isUndoCalled;
    private boolean isRedoCalled;
    private Stack<Shape> history;
    private Stack<Shape> undoneHistory;
    private DrawingMode mode;

    MyListener listener;

    public DrawingPanel(){
        this.mode = DrawingMode.STROKE;
        background = Colors.BLACK;
        foreground = Colors.WHITE;
        history = new Stack<Shape>();
        undoneHistory = new Stack<Shape>();

        listener = new MyListener(this);

        addMouseMotionListener(listener);
        addMouseListener(listener);

        clearScreen = true;
    }

    public void setMode(DrawingMode mode){
        this.mode = mode;
    }

    public DrawingMode getMode(){
        return this.mode;
    }

    @Override
    public void paintComponent(Graphics g){
        // clear the screan
        clear(g);
        // render everything in history
        for(Shape shape:history){
            shape.render(g);
        }
    }

    public void addShape(Shape shape){
        history.add(shape);
    }

    public void clear(){
        history.clear();
        undoneHistory.clear();
        repaint();
    }

    public void undo(){
        undoneHistory.push(history.pop());
        repaint();
    }

    public void redo(){
        history.push(undoneHistory.pop());
        repaint();
    }

    public void removeShape(){
        history.pop();
    }

    public void setColor(Color color){
        foreground = color;
    }

    public Color getColor(){
        return foreground;
    }

    private void clear(Graphics g){
        width = getWidth();
        height = getHeight();
        g.setColor(background);
        g.fillRect(0 , 0, width, height);
    }

    public void incrementBrushSize(){
        brushSize++;
    }

    public void decrementBrushSize(){
        if(brushSize != 1)
            brushSize--;
    }

    public int getBrushSize(){
        return this.brushSize;
    }


    private class MyListener extends MouseInputAdapter{
        
        private Shape currentShape;
        private DrawingPanel panel;

        public MyListener(DrawingPanel panel){
            super();
            this.panel = panel;
        }

        public Shape getCurrentShape(){
            return currentShape;
        }

        public void mousePressed(MouseEvent e){
            switch(panel.getMode()){
                case STROKE:
                    currentShape = new Stroke();
                    Stroke currStroke = (Stroke)(currentShape);
                    currStroke.setBrushSize(panel.getBrushSize());
                    currStroke.addNode(e.getX(), e.getY());
                    break;
                case RECT:
                    currentShape = new Rectangle(e.getX(), e.getY());
                    break;
            }
            currentShape.setColor(panel.getColor());
            panel.addShape(currentShape);
            panel.revalidate();
            panel.repaint();
        }

        public void mouseDragged(MouseEvent e){
            switch(panel.getMode()){
                case STROKE:
                    Stroke currStroke = (Stroke)(currentShape);
                    currStroke.addNode(e.getX(), e.getY());
                    break;
                case RECT:
                    Rectangle currRect = (Rectangle)(currentShape);
                    currRect.setEnd(e.getX(), e.getY());
                    break;
            }
            panel.revalidate();
            panel.repaint();
        }

        public void mouseReleased(MouseEvent e){
            panel.setMode(DrawingMode.STROKE);
            currentShape = null;
        }
    }
}
