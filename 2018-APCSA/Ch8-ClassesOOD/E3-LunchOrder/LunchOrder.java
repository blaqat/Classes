public class LunchOrder
{
    public static void main(){
        Food hamburger = new Food("Hamburger", 1.85, 9, 33, 1);
        Food salad = new Food("Salad", 2, 1, 11, 5);
        Food frenchfries = new Food("French Fries", 1.3, 11, 36, 4);
        Food soda = new Food("Soda", .95, 0 ,38, 0);

        Misc.print("Enter number of hamburgers: ");
        int burgers = Misc.keyboard.nextInt();
        Misc.println(hamburger+"\n");

        Misc.print("Enter number of salads: ");
        int salads = Misc.keyboard.nextInt();
        Misc.println(salad+"\n");

        Misc.print("Enter number of fries: ");
        int fries = Misc.keyboard.nextInt();
        Misc.println(frenchfries+"\n");

        Misc.print("Enter number of sodas: ");
        int sodas = Misc.keyboard.nextInt();
        Misc.println(soda+"\n");

        Misc.println(Misc.moneyFormat(soda.getPrice() * sodas + frenchfries.getPrice() * fries + salad.getPrice() * salads + hamburger.getPrice() * burgers));
    }
}
