package Hw6;
import java.util.Scanner;

public class NegativeCycle {
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
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int m = input.nextInt();
        int[] D = new int[n + 1];
        int[][] edges = new int[m][3];

        for(int i = 0; i < m; i++){
            int[] edge = {
                    input.nextInt(), 
                    input.nextInt(), 
                    input.nextInt()
            };

            edges[i] = edge;
        }

        for(int i = 2; i <= n; i++){
            D[i] = Integer.MAX_VALUE;
        }

        boolean found = true;

        for(int i = 0; i < n && found; i++){
            found = false;

            for(int[] edge: edges){
                int u = D[edge[0]];
                int d = u + edge[2];

                if(u != Integer.MAX_VALUE && d < D[edge[1]]){
                    D[edge[1]] = d;
                    found = true;
                }
            }
            
        }

        found = false;

        for(int[] edge : edges){
            int u = D[edge[0]];
            int v = D[edge[1]];
            int d = u + edge[2];
            if (u != Integer.MAX_VALUE && d < v) {
                found = true;
                break;
            }
        }

        log(found?"YES":"NO", true);
    }
}
