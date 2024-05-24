
/**
 * Write a description of class LunchOrder here.
 *
 * @author (your name)
 * @version (a version number or a ++
 * ate)
 */
public class OldLunchOrder
{
    private static class Food
    {
        private double price;
        private int fat;
        private int carbs;
        private int fiber;

        public Food(){
            price = 0;
            fat = 0;
            carbs = 0;
            fiber = 0;
        }

        public Food(double p, int f, int c, int fi){
            price = p;
            fat = f;
            carbs = c;
            fiber = fi;
        }
    }
    public static void main(String[] args){
        Food hamburger = new Food(1.85,9,33,1);
        Food salad = new Food(2,1,11,5);
        Food fries = new Food(1.3,11,36,4);
        Food soda = new Food(.95,0,38,0);

        double total = 0;

        Misc.print("Enter number of hamburgers: ");
        total+=hamburger.price*Misc.keyboard.nextInt();
        Misc.println("Each hamburger has "+hamburger.fat+"g of fat. "+hamburger.carbs+"g of carbs, and "+hamburger.fiber+"g of fiber.");
        Misc.print("Enter number of salads: ");
        total+=salad.price*Misc.keyboard.nextInt();
        Misc.println("Each salad has "+salad.fat+"g of fat. "+salad.carbs+"g of carbs, and "+salad.fiber+"g of fiber.");
        Misc.print("Enter a number of fries: ");
        total+=fries.price*Misc.keyboard.nextInt();
        Misc.println("Each fries has "+fries.fat+"g of fat. "+fries.carbs+"g of carbs, and "+fries.fiber+"g of fiber.");
        Misc.print("Enter a number of soda: ");
        total+=soda.price*Misc.keyboard.nextInt();
        Misc.println("Each soda has "+soda.fat+"g of fat. "+soda.carbs+"g of carbs, and "+soda.fiber+"g of fiber.");

        Misc.printTitle("Total: "+Misc.moneyFormat(total));
    }
}
