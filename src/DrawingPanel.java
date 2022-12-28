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
        // renders the stroke detected by the listener
        if(listener.isDrawing()){
            listener.getCurrentShape().setColor(foreground);
            listener.getCurrentShape().render(g);
            if(listener.getCurrentShape() instanceof Stroke){
                Stroke currStroke = (Stroke)(listener.getCurrentShape());
                currStroke.setBrushSize(brushSize);
            }
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

    private class MyListener extends MouseInputAdapter{
        private boolean isDrawing = false;

        private Shape currentShape;
        private DrawingPanel panel;

        public MyListener(DrawingPanel panel){
            super();
            this.panel = panel;
        }

        public Shape getCurrentShape(){
            return currentShape;
        }

        public boolean isDrawing(){
            return isDrawing;
        }

        public void mousePressed(MouseEvent e){
            isDrawing = true;
            switch(panel.getMode()){
                case STROKE:
                    currentShape = new Stroke();
                    Stroke currStroke = (Stroke)(currentShape);
                    currStroke.addNode(e.getX(), e.getY());
                    break;
                case RECT:
                    System.out.println("making rect...");
                    currentShape = new Rectangle(e.getX(), e.getY());
                    panel.addShape(currentShape);
                    break;
            }
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
            isDrawing = false;
            panel.addShape(currentShape);
            currentShape = null;
        }
    }
}
