import java.util.Scanner;

class KnapsackWeightBoundsWithSolution {
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

	public static int find_max_cost(int n, int[] w, int[] c,int w_min, int w_max){
		int[][] costs = new int[n+1][w_max+1];
		int[][] weights = new int[n+1][w_max+1];

        for (int i = 1; i <= w_max; i++) {
            for (int j = 1; j <= n; j++) {
				int wj = w[j-1];
                costs[j][i] = costs[j-1][i];
				weights[j][i] = weights[j-1][i];
                if (wj <= i && (costs[j-1][i-wj] + c[j-1] > costs[j][i])) {
					costs[j][i] = costs[j-1][i-wj] + c[j-1];
					weights[j][i] = weights[j-1][i-wj] + w[j-1];
                }
            }
        }

		int max_cost = -1;
		int max_weight = -1;

		for (int j = 0; j <= n; j++) {
            for (int i = 0; i <= w_max; i++) {
				
				if(weights[j][i] < w_min || weights[j][i] > w_max){
					weights[j][i] = -1;
				}
				else if(costs[j][i] > max_cost) {
					max_cost = costs[j][i];
					max_weight = weights[j][i];
				}

/* 				if(weights[j][i] < 10 && weights[j][i] >=0)
					log("0" + weights[j][i], false);
				else
					log(weights[j][i], false); */
				//log_pair(weights[j][i], costs[j][i], false);
				//log(" ", false);
				
			}
			//log("", true);
		}
		log(max_cost, true);
		boolean[] items = new boolean[n];
		int s = 0;
		int m = n;
		int j = w_max;
		boolean b = false;
		
		while(j > 0 && m>0 && s < max_cost){
/* 			log_pair(m, j, false);
			log_pair(c[m-1], w[m-1], false);
			log("\t", false); */
			//log_pair(costs[m-1][j - w[m-1]] + c[m-1], costs[m-1][j], true);
			if(j - w[m-1] < 0){ // Idk how to fix this I literally followed the instructions
				
			} else {
			if(costs[m-1][j - w[m-1]] + c[m-1] >= costs[m-1][j]){
				//log_pair("AAAAAAAAAAAAAA " + c[m-1], w[m-1], true);
				items[m-1] = true;
				b = !b;
				s += c[m-1];
				m--;
			}}
			if(!b)
				m--;
			else
				j--;
			
		}

		

		for(int i = 0; i < n; i++){
			if(items[i])
				log(i+1 + " ", false);
		}

		//log(max_weight, true);

		return max_cost;
	}
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		int n  = input.nextInt();
		int w_min = input.nextInt();
		int w_max = input.nextInt();

		int[] w = new int[n];
		int[] c = new int[n];

		for(int i = 0; i < n; i++){
			w[i] = input.nextInt();
			c[i] = input.nextInt();
		}
		find_max_cost(n, w, c, w_min, w_max);
		//log(, true);
	}
}