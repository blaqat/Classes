
/**
 * Write a description of class Coin here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class OldCoin
{
    static int flipCoin() {
        return Misc.random(0,1);
    }

    public static void main(String[] args) {
        OldCoin nickel = new OldCoin();
        if (nickel.flipCoin() == 0) {
            System.out.println("Heads up!");
        } else {
            System.out.println("Tails up!");
        }
    }
}
