/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class LeapYear
{
    static void print(String str){
        System.out.print(str);
    }

    static double round(double num,double decimal){
        return (double)(Math.round(num*Math.pow(10,decimal))/decimal);   
    }

    static void println(String str){System.out.println(str); }

    static void error(String str){println("Error; "+str);}

    static Boolean isALeapYear(int year){
        if ((year%4==0 && year%100!=0) || year%400==0) return true;
        else return false;
    }

    static int daysInAMonth(int[] date) // function daysInAMonth(...)
    {
        int[] months = {31,28,31,30,31,30,31,31,30,31,30,31};
        if(isALeapYear(date[2])) months[1] = 29;
        return months[date[0]-1];
    }

    static int[] getDate(){
        int[] date = new int[3];
        Scanner keyboard = new Scanner(System.in);
        print("Enter the month: ");
        date[0] = keyboard.nextInt();
        if(date[0]<1){date[0] = 1; error("Invalid month, setting month to January");}
        else if(date[0]>12){date[0] = 12; error("Invalid month, setting month to December");}
        print("Enter the day: ");
        date[1] = keyboard.nextInt();  
        print("Enter the year: ");
        date[2] = keyboard.nextInt(); 
        if(date[1]<1){date[1] = 1;error("Invalid day, setting day to 1");}
        else if(date[1]>daysInAMonth(date)){date[1]=daysInAMonth(date); error("Invalid day, setting day to "+daysInAMonth(date));}
        return date;
    }

    static String toDate(int[] date){
        return ""+date[0]+"/"+date[1]+"/"+date[2];
    }

    static int calculateDaysLeft(int[] date, String type){
        if(type == "month"){
            return daysInAMonth(date) - date[1];
        }
        else if(type == "year"){
            int days = calculateDaysLeft(date,"month");
            for(int i = date[0]+1;i<=12&&date[0]!=12;i++){
                int[] a = {i,0,date[2]};
                days += daysInAMonth(a);
            }            
            return days;
        }
        else {error("No date inputted"); return 1;}
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("CALCULATING DAYS INBETWEEN");

        println("******************************");
        println("Enter your first date: ");
        int[] date = getDate();
        println("******************************");
        println("Enter your second date: ");
        int[] date2 = getDate();
        println("******************************");

        int days = calculateDaysLeft(date,"year")-calculateDaysLeft(date2,"year");
        for(int i=date[2]+1;i<=date2[2]&&date[2]!=date2[2]; i++){
            int years = 365;
            if(isALeapYear(i))years=366; 
            days += years;
        }

        print("There are " + days + " days in between " + toDate(date) + " and " + toDate(date2));
    }
}