import java.util.Scanner;

public class AINegativeCycle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[][] edges = new int[m][3];

        for (int i = 0; i < m; i++) {
            edges[i][0] = scanner.nextInt();
            edges[i][1] = scanner.nextInt();
            edges[i][2] = scanner.nextInt();
        }

        System.out.println(hasNegativeWeightCycle(n, edges) ? "YES" : "NO");
    }

    public static boolean hasNegativeWeightCycle(int n, int[][] edges) {
        int[] distance = new int[n + 1];

        for (int i = 2; i <= n; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        distance[1] = 0;

        for (int i = 0; i < n; i++) {
            boolean updated = false;
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                int w = edge[2];

                if (distance[u] != Integer.MAX_VALUE && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    updated = true;
                }
            }
            if (!updated) {
                break;
            }
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];

            if (distance[u] != Integer.MAX_VALUE && distance[u] + w < distance[v]) {
                return true;
            }
        }

        return false;
    }
}
