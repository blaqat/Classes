import java.util.ArrayList;
import java.util.Scanner;

//Input: A single nonnegative integer, n, not larger than 10,000. 
//Output: A list of all of the prime numbers less than or 
//equal to n, in increasing order, each on its own line. 

class Primes {

    public static boolean isPrime(int integer){
        if(integer < 2)
            return false;

        int counter;
        for(counter = 2; integer%counter!=0; counter++){}

        return counter == integer;
    }

    public static String getPrimesUpTo(int integer){
        String primes = "";

        for(int i = 2; i <= integer; i++){
            if(isPrime(i))
                primes += i + "\n";
        }

        return primes;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int user_integer = input.nextInt();
        String primes = getPrimesUpTo(user_integer);

        System.out.print(primes);

    }
}