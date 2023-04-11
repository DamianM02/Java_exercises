package hojna;

import java.awt.*;

public class Branch implements XmasShape{
    int x;
    int y;
    double scale;

    Branch(int x, int y, double s){
        this.x=x;
        this.y=y;
        scale=s;
    }


    public void render(Graphics2D g2d){
        g2d.setColor(Color.GREEN);
        //g2d.fillOval(0,0,100,100);
        int[] x= { 0, 1, 2};
        int[] y= {1, 0, 1};
        g2d.fillPolygon(x, y, x.length );
    }

    public void transform(Graphics2D g2d){
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}
