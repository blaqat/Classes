package Hw3;
import java.util.Scanner;

class LongestIncreasingSubseqRecursive {
	public static void log(Object a, boolean nl){
		String log = "" + a;
		if(nl)
			System.out.println(log);
		else 
			System.out.print(log);
	}

	public static void log_pair(Object a, Object b, boolean nl){
		String log = "(" + a + ", " + b + ")";
		if(nl)
			System.out.println(log);
		else 
			System.out.print(log);
	}

	public static int incrSubseqRecursive(int j, int[] a){
		if(j == 0) return 1;

		int max_length = 0;

		for(int i = j - 1; i >= 0; i--){

			if(a[i] < a[j]){
				int length = incrSubseqRecursive(i, a);
				if (length > max_length) 
					max_length = length;

			}

		}
		
		return max_length+1;
	}

	public static void get(int n, int[] a){
		int max_length = 0;

		for(int j = 0; j < n; j++){
			int length = incrSubseqRecursive(j, a);

			if(length > max_length)
				max_length = length;
		}

		log(max_length, true);
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		int n = input.nextInt();
		int[] a = new int[n];
		for(int i = 0; i < n; i++){
			a[i] = input.nextInt();
		}

		get(n, a);
	}
}