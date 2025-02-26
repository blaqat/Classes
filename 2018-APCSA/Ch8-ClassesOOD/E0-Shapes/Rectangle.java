
public class Rectangle
{
    private double length,width;

    public Rectangle()
    {
        setLength(0);
        setWidth(0);
    }

    public Rectangle(double l, double w)
    {
        setLength(l);
        setWidth(w);
    }

    public void setLength(double l)
    {
        length = l;
    }

    public void setWidth(double w)
    {
        width = w;
    }

    public double getLength(){
        return length;
    }
 
    public double getWidth(){
        return width;
    }    
    
    public double getArea()
    {
        return length*width;
    }

    public double getPerimeter()
    {
        return length * 2 + width * 2; 
    }

    public static void displayAreaFormula(){
        System.out.println("Area: length * width");
    }

    public boolean equals(Rectangle sq){
        return (sq.length == length) && (sq.width == width);
    }

    public String toString(){
        return "Length: " + getLength() + " | Width: " + getWidth();
    }
}
