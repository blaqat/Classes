package Labs2;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapePanel extends JPanel
{
    public ShapePanel()
    {
        setBackground(Color.WHITE);
        setVisible(true);
    }

    public void update(Graphics window)
    {
        paint(window);
    }

    /*
     *All of your test code should be placed in paint.
     */
    public void paint(Graphics window)
    {
        window.setColor(Color.WHITE);
        window.fillRect(0,0,getWidth(), getHeight());
        window.setColor(Color.BLUE);
        window.drawRect(20,20,getWidth()-40,getHeight()-40);
        window.setFont(new Font("TAHOMA",Font.BOLD,18));
        window.drawString("PAC-MAN!",40,40);


        Shape pacman = new Shape(50, 50, 100, 100, Color.YELLOW);
        pacman.draw(window);

        Shape redpac = new Shape(50, 200, 100, 50, Color.RED);
        redpac.draw(window);

        Shape pacgirl = new Shape(50, 300, 50, 100, Color.PINK);
        pacgirl.draw(window);
    }
}