package Hw5;
import java.util.Scanner;

public class SwitchDirection {
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

        for (int i = 0; i < G[v].length; i++) {
            if (G[v][i] != 0 && !seen[G[v][i] - 1]) {
                depth_first_search(G, seen, G[v][i] - 1);
            }
        }

    }

    static boolean is_connected(int[][] G){
        boolean[] seen = new boolean[G.length];

        depth_first_search(G, seen, 0);

        for(boolean has_seen : seen){
            if(!has_seen)
                return false;
        }

        return true;
    }

    static boolean is_strongly_connected(int[][] G) {
        if (!is_connected(G))
            return false;
    
        int[][] GT = new int[G.length][101];
        for (int u = 0; u < G.length; u++) {
            for (int j = 0; G[u][j] != 0; j++) {
                int v = G[u][j];
                int index = 0;
    
                while (GT[v - 1][index] != 0) {
                    index++;
                }
    
                GT[v - 1][index] = u + 1;
            }
        }
    
        return is_connected(GT);
    }
    

    static void transpose_vertex(int[][] G, int u, int v) {
        u--;
    
        int i = 0;
        while (G[u][i] != v && G[u][i] != 0) {
            i++;
        }
    
        if (G[u][i] == v) {
            for (int j = i; j < G[u].length - 1; j++) {
                G[u][j] = G[u][j + 1];
            }

            G[u][G[u].length - 1] = 0;
    
            int j = 0;
            while (G[v - 1][j] > u + 1) {
                j++;
            }
    
            for (int k = G[v - 1].length - 2; k >= j; k--) {
                G[v - 1][k + 1] = G[v - 1][k];
            }

            G[v - 1][j] = u + 1;
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
    
        int n = input.nextInt();
        int G[][] = new int[n][101];
       // int GT[][] =  new int[n][101];

        for(int i = 0; i < n; i++){
            int adj = input.nextInt();
            for(int adj_i = 0; adj != 0; adj_i++, adj = input.nextInt()){
                G[i][adj_i] = adj;
                //GT[adj_i][i] = adj;
            }
        }

        boolean found = false;
        int found_u = 0, found_v = 999;
        
        for(int u = 0; u < n && !found; u++){
           //log(u+1 + ": ", false);
            for(int j = 0; G[u][j]!=0; j++){
                int uv = G[u][j];
                //log_pair(u+1, uv, false);
                transpose_vertex(G, u+1, uv);
                if(is_strongly_connected(G)){
                    if(!found){
                        found = true;
                        found_u = u+1;
                        found_v = uv;
                    } else found_v = Math.min(found_v, uv);
                }
                //log(is_strongly_connected(G), false);
                transpose_vertex(G, uv, u+1);
            }
          // log(" ", true);
        }

        log(found?"YES":"NO", true);
        if(found) log(found_u + " " + found_v, true);

    }
}