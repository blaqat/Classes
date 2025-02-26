import java.util.Scanner;

public class AINumPaths {

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

        int s = sc.nextInt();
        int t = sc.nextInt();

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            graph.addEdge(u, v);
        }

        int[] distance = new int[graph.n + 1];
        int[] paths = new int[graph.n + 1];

        dijkstrasAlgorithm(graph, s, distance, paths);
        int pathsCount = paths[t];

        System.out.println(pathsCount/2);
    }

    static void dijkstrasAlgorithm(Graph graph, int s, int[] distance, int[] paths) {
        boolean[] inH = new boolean[graph.n + 1];
        for (int i = 1; i <= graph.n; i++) {
            distance[i] = Integer.MAX_VALUE;
            inH[i] = true;
        }

        distance[s] = 0;
        paths[s] = 1;
        update(graph, s, distance, paths, inH);

        for (int i = 1; i < graph.n; i++) {
            int u = extractMinVertex(graph, distance, inH);
            update(graph, u, distance, paths, inH);
        }
    }

    static int extractMinVertex(Graph graph, int[] distance, boolean[] inH) {
        int minVertex = -1;
        for (int i = 1; i <= graph.n; i++) {
            if (inH[i] && (minVertex == -1 || distance[i] < distance[minVertex])) {
                minVertex = i;
            }
        }
        inH[minVertex] = false;
        return minVertex;
    }

    static void update(Graph graph, int u, int[] distance, int[] paths, boolean[] inH) {
        for (int v = 1; v <= graph.n; v++) {
            if (graph.adjMatrix[u][v] == 1 && inH[v]) {
                int newDistance = distance[u] + graph.adjMatrix[u][v];
                if (distance[v] > newDistance) {
                    distance[v] = newDistance;
                    paths[v] = paths[u];
                } else if (distance[v] == newDistance) {
                    paths[v] += paths[u];
                }
            }
        }
    }
}
