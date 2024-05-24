import java.util.Scanner;

public class StronglyConnectedComponents {

    private static int time = 0;
    private static int[][] G, GT;
    private static boolean[] seen;
    private static int[] finishTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // Number of vertices in the graph
        int m = scanner.nextInt(); // Number of edges in the graph

        G = new int[n][n];
        GT = new int[n][n];
        seen = new boolean[n];
        finishTime = new int[n];

        // Read the adjacency list for the graph
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            G[u][v] = 1;
        }

        // Initialize seen array and finish time array
        for (int v = 0; v < n; v++) {
            seen[v] = false;
            finishTime[v] = 0;
        }

        // Perform DFS on G
        for (int s = 0; s < n; s++) {
            if (!seen[s]) {
                dfs(G, s);
            }
        }

        // Compute G^T by reversing all edges of G
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                GT[i][j] = G[j][i];
            }
        }
7
3 0
7 0
2 0
1 2 7 0
3 1 0
4 0
6 0
public int[] merge(int[] half1, int[] half2, boolean byLarger) {
    int i = 0, j = 0, k = 0;

    int[] output = new int[half1.length + half2.length];

    while (i < half1.length && j < half2.length) {
        if (half1[i] == 0) {
            i++;
            continue;
        }
        if (half2[j] == 0) {
            j++;
            continue;
        }
        
        if (!byLarger && half1[i] <= half2[j] || byLarger && half1[i] >= half2[j]) {
            output[k] = half1[i];
            i++;
        } else {
            output[k] = half2[j];
            j++;
        }
        k++;
    }

    while (i < half1.length) {
        if (half1[i] != 0) {
            output[k] = half1[i];
            k++;
        }
        i++;
    }

    while (j < half2.length) {
        if (half2[j] != 0) {
            output[k] = half2[j];
            k++;
        }
        j++;
    }

    return output;
}



        // Reset the seen array
        for (int v = 0; v < n; v++) {
            seen[v] = false;
        }

        // Process vertices by decreasing finish time
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        java.util.Arrays.sort(order, (a, b) -> Integer.compare(finishTime[b], finishTime[a]));

        // Perform DFS on G^T and output strongly connected components
        for (int v : order) {
            if (!seen[v]) {
                dfs(GT, v);
                System.out.println(); // Print a new line for each strongly connected component
            }
        }
    }

    private static void dfs(int[][] graph, int v) {
        seen[v] = true;
        for (int u = 0; u < graph.length; u++) {
            if (graph[v][u] == 1 && !seen[u]) {
                dfs(graph, u);
            }
        }
        finishTime[v] = time++;
        System.out.print(v + " ");
    }
}
