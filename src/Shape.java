import java.awt.Graphics;
import java.awt.Color;

public interface Shape{
	public void render(Graphics g);
	public boolean isDone();
	public void setColor(Color color);
	public Color getColor();
}
