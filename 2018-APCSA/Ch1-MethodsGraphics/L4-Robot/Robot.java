package Labs2;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Canvas;

class Robot extends Canvas
{
   public Robot()    //constructor method - sets up the class
   {
      setSize(800,600);
      setBackground(Color.WHITE);       
      setVisible(true);
   }

   public void paint( Graphics window )
   {
      window.setColor(Color.BLUE);

      window.drawString("Robot LAB ", 35, 35 );

      head(window);
      upperBody(window);
      lowerBody(window);
      
      
   }

   public void head( Graphics window )
   {
      window.setColor(Color.GRAY);

      window.fillOval(300, 100, 200, 150);
      
      window.setColor(Color.BLACK);
      
      window.fillRect(375, 125, 50, 50);
      window.fillRect(397, 50, 5, 50);
      window.fillArc(350, 150, 100, 75, -180, 180);
      window.setColor(Color.GREEN);
      
      window.fillRect(380, 130, 40, 40);
      window.fillOval(390, 48, 20, 20);
        //add more code here
      
   }

   public void upperBody( Graphics window )
   {

        window.setColor(Color.GRAY);
        
        window.fillRect(350, 275, 100, 75);
        
        window.setColor(Color.BLACK);
        
        window.drawLine(350, 290, 300, 225);
        
        window.drawLine(450, 290, 500, 225);
   }

   public void lowerBody( Graphics window )
   {

        window.setColor(Color.GRAY);
        
        window.fillRect(325, 350, 150, 75);
        
        window.setColor(Color.GREEN);
        
        window.fillRect(312, 375, 175, 25);
        
        window.setColor(Color.BLACK);
        
        window.fillRect(300, 400, 200, 25);
        
        window.drawLine(375, 425, 365, 500);
        
        window.drawLine(425, 425, 435, 500);

   }
}