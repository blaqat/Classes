
public class Adder
{
    private static int score;
    public static void main(){
        Equation adder = new Equation();
        int choice;
        do{
            adder.generateEquation();
            int trial = 3;
            Misc.print(adder + " = ");
            choice = Misc.keyboard.nextInt();
            if(choice!=999){
                while(!adder.equals(choice) && trial > 0){
                    Misc.print("Wrong answer. Try again: ");
                    choice = Misc.keyboard.nextInt();
                    trial --;
                }
                score += trial;
            }
        }while(choice != 999);
        Misc.print("Your score is: " + score);
    }
}
