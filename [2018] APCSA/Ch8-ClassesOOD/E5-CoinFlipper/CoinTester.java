
/**
 * Write a description of class CoinTester here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CoinTester
{
    public static void main(String[] args) {
        Coin nickel = new Coin();
        nickel.flipCoin();
        Misc.print(""+nickel);
        /*
        if (nickel.flipCoin() == 0) {
            System.out.println("Heads up!");
        } else {
            System.out.println("Tails up!");
        }
        */
    }
}
