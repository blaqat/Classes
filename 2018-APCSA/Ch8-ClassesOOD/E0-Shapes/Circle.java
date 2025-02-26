
/* Aiden
 * Programs: Ch8
 * Programming - 4y
 * Finally O-OP :3
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
 */

public class Circle {
    private static final double PI = 3.14;
    private double radius;
    
    public Circle() {
        radius = 1; //default radius
    }

    public Circle(double rad){
        radius = rad;
    }
    
    public void setRadius(double newRadius) {
        radius = newRadius;
    }

    public double getRadius() {
        return(radius);
    }

    public double getArea(){
        return PI * Math.pow(radius,2);
    }
    
    public double getCircumference() {
        return 2 * PI * radius;
    }

}