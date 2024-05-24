/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class RandomWalk
{
    static void print(String str){
        System.out.print(str);
    }

    static void println(String str){
        System.out.println(str);
    }    

    static int random(int min, int max){
        max+=1;
        return min + (int)(Math.random() * (max-min));
    }

    static double round(double number, double decimal){
        return (double)Math.round(number*Math.pow(10,decimal));
    }

    static int walkTrial(){
        int walked = 7;
        int steps = 0;
        while(walked>=0&&walked<=14){
            int step = random(0,1);
            switch(step){
                case 0: walked+=2; break;
                case 1: walked-=2; break;
            }
            steps+=1;
        }
        return steps;
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("---STEPS-FOR-DIS-MAN-TO-GET-OFF-DIS-BRIDGE---");
        int trials = 0;
        int biggest = 0;
        for(int i=0;i!=50;i++){
            int trial = walkTrial();
            trials+=trial;
            if(trial>biggest)biggest=trial;
        }
        
        println("Average number of steps: " + trials/50);
        println("Greatest number of steps: " + biggest);

    }
}