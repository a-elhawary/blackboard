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

    MyListener listener;

    public DrawingPanel(){
        background = Colors.BLACK;
        foreground = Colors.WHITE;
        history = new Stack<Shape>();
        undoneHistory = new Stack<Shape>();

        listener = new MyListener(this);

        addMouseMotionListener(listener);
        addMouseListener(listener);

        clearScreen = true;
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
            listener.getCurrentStroke().setColor(foreground);
            listener.getCurrentStroke().setBrushSize(brushSize);
            listener.getCurrentStroke().render(g);
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
        private boolean isStrokeSavable = false;

        private Stroke currentStroke;
        private Stroke pastStroke;
        private DrawingPanel panel;

        public MyListener(DrawingPanel panel){
            super();
            this.panel = panel;
        }

        public boolean isDrawing(){
            return isDrawing;
        }

        public boolean isStrokeSavable(){
            return isStrokeSavable;
        }

        public Stroke getCurrentStroke(){
            return currentStroke;
        }

        public Stroke getPastStroke(){
            isStrokeSavable = false;
            return pastStroke;
        }
        
        public void mousePressed(MouseEvent e){
            isDrawing = true;
            currentStroke = new Stroke();
            currentStroke.addNode(e.getX(), e.getY());
            panel.revalidate();
            panel.repaint();
        }

        public void mouseDragged(MouseEvent e){
            currentStroke.addNode(e.getX(), e.getY());
            panel.revalidate();
            panel.repaint();
        }

        public void mouseReleased(MouseEvent e){
            isDrawing = false;
            panel.addShape(currentStroke);
            currentStroke = null;
        }
    }
}
