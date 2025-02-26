package Labs2;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Canvas;

public class BigHouse extends Canvas
{
   public BigHouse()  //constructor - sets up the class
   {
      setSize(800,600);
      setBackground(Color.WHITE);
      setVisible(true);
   }

   public void paint( Graphics window )
   {
      bigHouse(window);
   }

   public void bigHouse( Graphics window )
   {
      window.setColor(Color.BLUE);

      window.drawString( "BIG HOUSE ", 50, 50 );

      window.setColor(Color.BLUE);

      window.fillRect( 200, 200, 400, 400 );
      
      window.setColor(Color.RED);
      
      window.fillRect( 175,150, 450, 100);
      
      window.setColor(new Color(200, 200, 200));
      
      window.fillOval(575, 50, 50, 50);
      window.fillOval(550,75, 50, 50);
      window.fillOval(600,25,50,50);
      
      window.setColor(new Color(150,0,0));
      
      window.fillRect(550, 80, 50,75);
      
      window.setColor(Color.YELLOW);
      
      window.fillRect(250, 300, 100, 100);
      window.fillRect(450, 300, 100, 100);
      
      window.setColor(Color.WHITE);
      window.fillRect(335, 425, 125, 175);
   }
}