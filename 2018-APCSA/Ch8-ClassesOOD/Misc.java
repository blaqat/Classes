
import java.util.Scanner;

public class Misc
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
    static void clear(){
        System.out.print('\u000C');
    }
    static void printBar(){
        Misc.println("_________________________________________________________________");
    }
    static void printTitle(String title){
        Misc.printBar();
        Misc.println(title);
        Misc.printBar();
    }
    static int intLength(int integer){
        return (int)Math.log10(integer)+1;
    }
    static String stringReverse(String string){
        String news = "";
        for(int i = string.length();i > 0;i--){
            news=news+string.charAt(i-1);
        }
        return news;
    }
    static String moneyFormat(double money){
        money = (int)round(money,2);
        String format = "";
        String mula = stringReverse(""+(int)money);
        
        format = stringReverse(mula.substring(0,2))+".";
        mula = mula.substring(2);

        for(int i = 0;i<mula.length();i++){
            if(i%3==0&&i!=0){
                format=format+",";
            }
            format=format+mula.charAt(i);
        }
        return "$"+stringReverse(format);       
    }
    static Scanner keyboard = new Scanner(System.in);
}