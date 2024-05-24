package Labs2;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import java.awt.Color;
import java.awt.Graphics;

public class Shape
{
    //instance variables
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private Color color;

    public Shape(int x, int y, int wid, int ht, Color col)
    {
        setXPosition(x);
        setYPosition(y);
        setWidth(wid);
        setHeight(ht);
        setColor(col);
    }

    public void draw(Graphics window)
    {
        int halfx = width/2;
        int halfy = height/2;
        int fifx = width/5;
        int fify = height/5;

        window.setColor(color);

        window.fillArc(xPos, yPos, width, height,45, 270);

        window.setColor(Color.BLACK);

        window.fillRect(xPos + halfx - fifx/2, yPos + fify/2, fifx/2, fify);

        window.setColor(new Color(255,191,41));

        for(int i = 0; i<=2; i++)
        {
            window.fillOval(xPos+width + (i * halfx),yPos + halfy - (fify/2), fifx, fify);
        }
    }

    //BONUS
    //add in set and get methods for all instance variables
    public int getXPosition(){
        return xPos;
    }

    public int getYPosition(){
        return yPos;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setXPosition(int xP){
        xPos = xP;
    }

    public void setYPosition(int yP){
        yPos = yP;
    }

    public void setWidth(int wid){
        width = wid;
    }

    public void setHeight(int heig){
        height = heig;
    }
    
    public void setColor(Color col){
        color = col;
    }
    
    public String toString()
    {
        return "Pacman:\t"+xPos+" "+yPos+" "+width+" "+height+" "+color;
    }
}