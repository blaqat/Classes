import java.util.Scanner;

public class ConnectGraph {

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

    static void depth_first_search(int[][] G, boolean[] seen, int v) {
        seen[v] = true;

        for (int i = 1; i < G.length; i++) {
            if (G[v][i] == 1 && !seen[i]) {
                depth_first_search(G, seen, i);
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int n = input.nextInt();
        int m = input.nextInt();
        int k = 0;
        boolean[] seen = new boolean[n + 1];

        int[][] G = new int[n+1][n+1];

        for (int i = 0; i < m; i++) {
            int ep_1 = input.nextInt();
            int ep_2 = input.nextInt();

            G[ep_1][ep_2] = 1;
            G[ep_2][ep_1] = 1;
        }

        for (int i = 1; i <= n; i++) {
            if (!seen[i]) {
                depth_first_search(G, seen, i);
                k++;
            }
        }

        log(k - 1, true);
    }
}
