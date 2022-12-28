import java.awt.Color;
import java.awt.Graphics;

public class Rectangle implements Shape{
    // a rectangle can be defined by the starting and ending x,y pair
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    private int start[] = {0,0};

    // initialize a rectangle with starting x and y
    public Rectangle(int x, int y){
        // initialize start and end to point to the same point
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
        this.color = Colors.WHITE;
        start[0] = x;
        start[1] = y;
    }

    public void render(Graphics g){
        g.setColor(this.color);
        g.fillRect(this.start[0], this.start[1], this.width, this.height);
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }
    public void setEnd(int x, int y){
        this.width = x - this.x;
        this.height = y - this.y;

        if(this.width > 0 && this.height > 0){
            start[0] = this.x;
            start[1] = this.y;
        }else if(this.width > 0 && this.height < 0){
            this.height *= -1;
            this.start[0] = this.x;
            this.start[1] = y;
        }else if(this.width < 0 && this.height > 0){
            this.width *= -1;
            this.start[0] = x;
            this.start[1] = this.y;
        }else if(this.width < 0 && this.height < 0){
            this.width *= -1;
            this.height *= -1;
            this.start[0] = x;
            this.start[1] = y;
        }
    }
}
