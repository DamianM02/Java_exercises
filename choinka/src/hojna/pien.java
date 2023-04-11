package hojna;

import java.awt.*;

public class pien implements XmasShape{
    int x;
    int y;
    double scale;

    pien(int x, int y, double s){
        this.x=x;
        this.y=y;
        scale=s;
    }


    public void render(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        //g2d.fillOval(0,0,100,100);
        int[] x= { 0, 1, 1, 0};
        int[] y= {0, 0, 1, 1};
        g2d.fillPolygon(x, y, x.length );
    }

    public void transform(Graphics2D g2d){
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}
