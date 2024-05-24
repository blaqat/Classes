import java.util.Scanner;

class LongestIncreasingSubseqDP {
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


	public static void update_length(int[] lengths, int i, int[] a){
		int max_length = 0;

		for(int j = 0; j < i; j++){
			if(a[j] < a[i] && lengths[j] > max_length){
				max_length = lengths[j];
			}
		}

		lengths[i] = 1 + max_length;
	}

	public static void get(int n, int[] a){
		int[] lengths = new int[n];
		int[][] avgs = new int[n][n-1];
		for(int i = 0; i < n; i++){
			lengths[i] = 1;
		}

		for(int i = 1; i < n; i++){
			update_length(lengths, i, a);
		}

		int max_length = 0;

		for(int i = 0; i < n; i++){
			int length = lengths[i];

			if(length > max_length)
				max_length = length;
		}

		//log(max_length);

	}

	public static int getRandom(double min, double max){
	    double x = (Math.random()*((max-min)+1))+min;
	    return (int)x;
	}

	public static long t(){
		return System.nanoTime();
	}

	public static int[] ran(int n, int min, int max){
		int[] a = new int[n];

		for(int i = 0; i < n; i++){
			a[i] = getRandom(min, max);
			//log(a[i] + ",", false);
		}

		return a;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		long start;
		System.out.println("n\t|rec\t|dp\t|difference");
		log("-----------------------------------------",true);
		for(int i = 0; i < 10; i++){

			int n = getRandom(10000, 100000);
			int[] a = ran(n, 1, 50000);

			//log(n + " ", true);

			start = t();
			LongestIncreasingSubseqRecursive.get(n, a);
			long end1 = t() - start;

			start = t();
			LongestIncreasingSubseqDP.get(n, a);
			long end2 = t() - start;

			System.out.println(n + "\t|" + (end1/1000000.) + "\t|" + (end2/1000000.) + "\t|" + Math.abs((end2-end1)/1000000.));
			log("-----------------------------------------",true);
		}

	}
}

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

		//log(max_length, true);
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