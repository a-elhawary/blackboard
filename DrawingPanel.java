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
    private int brushSize = 10;
    private boolean clearScreen;
    private boolean isUndoCalled;
    private boolean isRedoCalled;
    private Stack<Stroke> history;
    private Stack<Stroke> undoneHistory;

    MyListener listener;

    // variable that stores the last index of the stroke rendered to resume from correct poisition
    private int lastIndexDrawn;
    private int lastIndexErased;
    private int lastIndexReDrawn;

    public DrawingPanel(){
        background = Color.decode("#202020");
        foreground = Color.decode("#FFFFFF");
        history = new Stack<Stroke>();
        undoneHistory = new Stack<Stroke>();

        listener = new MyListener();

        addMouseMotionListener(listener);
        addMouseListener(listener);

        clearScreen = true;
    }

    @Override
    public void paintComponent(Graphics g){
        if(clearScreen){
            clear(g);
            clearScreen = false;
        }
        // renders the stroke detected by the listener
        if(listener.isDrawing()){
            g.setColor(foreground);
            lastIndexDrawn = listener.getCurrentStroke().renderStroke(g,lastIndexDrawn, brushSize);
        }

        if(listener.isStrokeSavable()){
            lastIndexDrawn = 0;
            history.push(listener.getPastStroke());
        }

        // draws the newest stroke with the background color
        if(isUndoCalled){
            if(history.empty()){
                isUndoCalled = false;
            }else{
                g.setColor(background);
                Stroke erasingStroke = history.pop();
                undoneHistory.push(erasingStroke);
                lastIndexErased = erasingStroke.renderStroke(g, lastIndexErased, brushSize);
                if(erasingStroke.getNumberOfNodes() >= lastIndexErased){
                    isUndoCalled = false;
                    lastIndexErased = 0;
                }
            }
        }

        if(isRedoCalled){
            if(undoneHistory.empty()){
                isRedoCalled = false;
            }else{
                g.setColor(foreground);
                Stroke reDrawing = undoneHistory.pop();
                history.push(reDrawing);
                reDrawing.renderStroke(g, lastIndexReDrawn, brushSize);
                if(reDrawing.getNumberOfNodes() >= lastIndexErased){
                    isRedoCalled = false;
                    lastIndexReDrawn = 0;
                }
            }
        }
    }

    public void setClear(){
        clearScreen = true;
        repaint();
    }

    public void setUndo(){
        isUndoCalled = true;
        repaint();
    }

    public void setRedo(){
        isRedoCalled = true;
        repaint();
    }

    private void clear(Graphics g){
        width = getWidth();
        height = getHeight();
        g.setColor(background);
        g.fillRect(0 , 0, width, height);
    }

    private class MyListener extends MouseInputAdapter{
        private boolean isDrawing = false;
        private boolean isStrokeSavable = false;

        private Stroke currentStroke;
        private Stroke pastStroke;


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
            repaint();
        }

        public void mouseDragged(MouseEvent e){
            currentStroke.addNode(e.getX(), e.getY());
            repaint();
        }

        public void mouseReleased(MouseEvent e){
            isDrawing = false;
            pastStroke = currentStroke;
            isStrokeSavable = true;
        }
    }
}