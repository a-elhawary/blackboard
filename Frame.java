import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Frame extends JFrame{

    DrawingPanel panel;
    final int C = 67;
    final int R = 82;
    final int Z = 90;

    public Frame(){
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
                    panel.setClear();
                    break;
                case R:
                    panel.setRedo();
                    break;
                case Z:
                    panel.setUndo();
                    break;
            }
        }
    }
}