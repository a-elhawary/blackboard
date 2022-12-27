import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Frame extends JFrame{

    DrawingPanel panel;
    
    // ASCII characters
    final int C = 67;
    final int R = 82;
    final int S = 83;
    final int Z = 90;
    final int minus = 45;
    final int plus = 61;
    final int one = 49;
    final int two = 50;
    final int three = 51;
    final int four = 52;
    final int five = 53;
    final int six = 54;
    final int seven = 55;

    int width = 1200;
    int height = 800;

    public Frame(){
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        add(panel);
        addKeyListener(new MyKeyListener());
        setVisible(true);
    }

    public static void main(String args[]){
        new Frame();
    }

    private class MyKeyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case C:
                    panel.clear();
                    break;
                case R:
                    panel.redo();
                    break;
                case Z:
                    panel.undo();
                    break;
                case minus:
                    panel.decrementBrushSize();
                    break;
                case plus:
                    panel.incrementBrushSize();
                    break;
                case one:
                    panel.setColor(Colors.WHITE);
                    break;
                case two:
                    panel.setColor(Colors.RED);
                    break;
                case three:
                    panel.setColor(Colors.BLUE);
                    break;
                case four:
                    panel.setColor(Colors.GREEN);
                    break;
                case five:
                    panel.setColor(Colors.YELLOW);
                    break;
                case six:
                    panel.setColor(Colors.PURPLE);
                    break;
                case seven:
                    panel.setColor(Colors.PINK);
                    break;
		case S:
		    System.out.println("making square");
		default:
		    System.out.println(e.getKeyCode());
            }
        }
    }
}
