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
            listener.getCurrentStroke().setColor(foreground);
            listener.getCurrentStroke().setBrushSize(brushSize);
            listener.getCurrentStroke().render(g);
        }

        if(listener.isStrokeSavable()){
            history.push(listener.getPastStroke());
        }

        // draws the newest stroke with the background color
        if(isUndoCalled){
            if(history.empty()){
                isUndoCalled = false;
            }else{
				Shape erasingStroke = history.pop();
        		undoneHistory.push(erasingStroke);
				Color original = erasingStroke.getColor();
        		erasingStroke.setColor(background);
        		erasingStroke.render(g);
				if(erasingStroke.isDone()){
					erasingStroke.setColor(original);
        			isUndoCalled = false;
				}
            }
        }

        if(isRedoCalled){
            if(undoneHistory.empty()){
                isRedoCalled = false;
            }else{
				Shape redrawingStroke = undoneHistory.pop();
        		history.push(redrawingStroke);
        		redrawingStroke.render(g);
				if(redrawingStroke.isDone()){
        			isRedoCalled = false;
				}
            }
        }
    }

	private void performUndo(Stack<Shape> outOf, Stack<Shape> into, boolean flag, Graphics g){
		
	}
    public void setClear(){
        clearScreen = true;
        history.clear();
        undoneHistory.clear();
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
        brushSize--;
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
