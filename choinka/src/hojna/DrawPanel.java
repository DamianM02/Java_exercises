package hojna;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {

    List<XmasShape> shapes = new ArrayList<>();



    public void paintComponent(Graphics g){
        Graphics2D g2d=(Graphics2D)g;
        g2d.setColor(Color.GREEN);
        //g2d.fillOval(0,0,100,100);
        int[] x=new int[] {1, 2, 3};
        int[] y=new int[] {2, 1, 2};
        g2d.fillPolygon(x, y, x.length );

        super.paintComponent(g);
        for(XmasShape s:shapes){
            s.draw((Graphics2D)g);
        }

    }

    void add(XmasShape i){
        shapes.add(i);
    }



    DrawPanel(){
        add(new Branch(100,10,100 ));
        add(new Branch(50,60,150 ));
        add(new Branch(0,110,200 ));
        add(new Bubble(150, 50, 0.3, Color.YELLOW, Color.BLUE));
        add(new Bubble(190, 230, 0.3, Color.YELLOW, Color.RED));
        add(new Bubble(60, 290, 0.3, Color.YELLOW, Color.RED));
        add(new Bubble(250, 300, 0.3, Color.YELLOW, Color.BLUE));
        add(new Bubble(100, 240, 0.3, Color.YELLOW, Color.YELLOW));
        add(new Bubble(150, 120, 0.3, Color.YELLOW, Color.YELLOW));

        add(new Bubble(280, 150, 0.3, Color.YELLOW, Color.BLUE));
        add(new Bubble(225, 70, 0.3, Color.YELLOW, Color.RED));
        add(new Bubble(250, 200, 0.3, Color.YELLOW, Color.YELLOW));


        add(new pien(175, 310, 50));


    }
}
