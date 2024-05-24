import java.util.Scanner;

public class AIConnectGraph {

    static class Graph {
        int n;
        int[][] adjMatrix;

        Graph(int n) {
            this.n = n;
            adjMatrix = new int[n + 1][n + 1];
        }

        void addEdge(int u, int v) {
            adjMatrix[u][v] = 1;
            adjMatrix[v][u] = 1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        Graph graph = new Graph(n);

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            graph.addEdge(u, v);
        }

        int k = findMinEdgesToConnect(graph);
        System.out.println(k);
    }

    static int findMinEdgesToConnect(Graph graph) {
        boolean[] visited = new boolean[graph.n + 1];
        int connectedComponents = 0;

        for (int i = 1; i <= graph.n; i++) {
            if (!visited[i]) {
                dfs(graph, visited, i);
                connectedComponents++;
            }
        }

        return connectedComponents - 1;
    }

    static void dfs(Graph graph, boolean[] visited, int currentNode) {
        visited[currentNode] = true;

        for (int i = 1; i <= graph.n; i++) {
            if (graph.adjMatrix[currentNode][i] == 1 && !visited[i]) {
                dfs(graph, visited, i);
            }
        }
    }
}
