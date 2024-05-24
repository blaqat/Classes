package Hw5;
import java.util.Scanner;

public class NumPaths {
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

    static int get_min_vertext(int[][] G, int[] dist, boolean[] seen) {
        int min = -1;

        for (int i = 1; i < G.length; i++) {
            if (seen[i] && (min == -1 || dist[i] < dist[min])) {
                min = i;
            }
        }

        seen[min] = false;

        return min;
    }

    static void update(int[][] G, int u, int[] dist, int[] parent, boolean[] seen) {
        for (int v = 1; v < G.length; v++) {
            if (G[u][v] == 1 && seen[v]) {

                int new_dist = dist[u] + G[u][v];

                if (dist[v] > new_dist) {
                    dist[v] = new_dist;
                    parent[v] = parent[u];

                } else if (dist[v] == new_dist) {
                    parent[v] += parent[u];
                }

            }
        }
    }

    static void dijkstra(int[][] G, int s, int[] dist, int[] parent) {
        boolean[] seen = new boolean[G.length];

        for (int i = 1; i < G.length; i++) {
            dist[i] = Integer.MAX_VALUE;
            seen[i] = true;
        }

        dist[s] = 0;
        parent[s] = 1;  
        update(G, s, dist, parent, seen);

        for (int i = 1; i < G.length - 1; i++) {
            int u = get_min_vertext(G, dist, seen);
            update(G, u, dist, parent, seen);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int n = input.nextInt();
        int m = input.nextInt();
        int s = input.nextInt();
        int t = input.nextInt();
        int[] dist = new int[n + 1];
        int[] parent = new int[n + 1];

        int[][] G = new int[n+1][n+1];
        for (int i = 0; i < m; i++) {
            int ep_1 = input.nextInt();
            int ep_2 = input.nextInt();
            G[ep_1][ep_2] = 1;
            G[ep_2][ep_1] = 1;
        }

        dijkstra(G, s, dist, parent);

        log(parent[t]/2, true);
    }

}
