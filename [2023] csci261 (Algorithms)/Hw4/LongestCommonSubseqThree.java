import java.util.Scanner;

class LongestCommonSubseqThree {
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


	public static void update_length(int[][][] lengths, int i, int[] a, int[] b, int[] c){

		for(int j = 1; j <= b.length; j++){
			for(int k = 1; k <= c.length; k++){
				if(a[i-1] == b[j-1] && a[i-1] == c[k-1]){
					lengths[i][j][k] = 1 + lengths[i-1][j-1][k-1];
				} else {
					lengths[i][j][k] = Math.max(Math.max(lengths[i-1][j][k], lengths[i][j-1][k]), lengths[i][j][k-1]);
				}
			}

		}

	}

	public static int[][][] find_common_subseq_len(int[] a, int[] b, int[] c){
		int[][][] lengths = new int[a.length+1][b.length+1][c.length+1];

		for(int i = 1; i <= a.length; i++){
			update_length(lengths, i, a, b, c);
		}

		return lengths;

	}

	public static int[] find_common_subseq(int[][][] lengths, int max_len, int[] a, int[] b, int[] c){
		
		int i = a.length; 
		int j = b.length; 
		int k = c.length;
		int[] seq = new int[max_len];
		int index = max_len;
		int x = 0;

		while (i > 0 && j > 0 && k > 0){
			x = lengths[i][j][k];
			if(a[i-1] == b[j-1] && a[i-1] == c[k-1]){
				seq[index-1] = a[i-1];
				i--;
				j--;
				k--;
				index--;
			} 
			else if (x == lengths[i-1][j][k])
				i--;
			else if (x == lengths[i][j-1][k])
				j--;
			else //(lengths[i][j][k-1] > lengths[i-1][j][k] && lengths[i][j][k-1] > lengths[i][j-1][k])
				k--;
		}
		

		return seq;
		
	}
	
	public static void main(String[] args) {
		try (Scanner input = new Scanner(System.in)) {
			int[] n = {input.nextInt(), input.nextInt(), input.nextInt()};

			int[] a = new int[n[0]];
			int[] b = new int[n[1]];
			int[] c = new int[n[2]];

			for(int i = 0; i < a.length; i++){
				a[i] = input.nextInt();
			}
			for(int i = 0; i < b.length; i++){
				b[i] = input.nextInt();
			}
			for(int i = 0; i < c.length; i++){
				c[i] = input.nextInt();
			}
			//int[] result = ;
			int[][][] common_lens = find_common_subseq_len(a, b, c);
			int len = common_lens[a.length][b.length][c.length];
			log(len,true);

			int[] seq = find_common_subseq(common_lens, len, a, b, c);

			for(int s : seq){
				log(s + " ", false);
			}
		}

		//print(find_common_subseq(result[2]));
	}
}