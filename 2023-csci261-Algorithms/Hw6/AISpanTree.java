import java.util.Scanner;
import java.util.Arrays;

public class AISpanTree {
    static class Edge {
        int u, v, w, f;

        Edge(int u, int v, int w, int f) {
            this.u = u;
            this.v = v;
            this.w = w;
            this.f = f;
        }
    }

    static int[] parent;
    // Implementation of merge sort algorithm
    public static void merge(Edge[] edges, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
    
        Edge[] L = new Edge[n1];
        Edge[] R = new Edge[n2];
    
        for (int i = 0; i < n1; ++i) {
            L[i] = edges[left + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = edges[middle + 1 + j];
        }
    
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i].f != R[j].f ? L[i].f > R[j].f : L[i].w < R[j].w) {
                edges[k] = L[i];
                i++;
            } else {
                edges[k] = R[j];
                j++;
            }
            k++;
        }
    
        while (i < n1) {
            edges[k] = L[i];
            i++;
            k++;
        }
    
        while (j < n2) {
            edges[k] = R[j];
            j++;
            k++;
        }
    }
    
    public static void mergeSort(Edge[] edges, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
    
            mergeSort(edges, left, middle);
            mergeSort(edges, middle + 1, right);
    
            merge(edges, left, middle, right);
        }
    }
    

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();
            int f = sc.nextInt();
            edges[i] = new Edge(u, v, w, f);
        }

        // Sort edges by weight in non-decreasing order, with F-edges having priority
        mergeSort(edges, 0, edges.length - 1);

        parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }

        int totalCost = 0;
        int fEdgesInTree = 0;
        int components = n;

        for (Edge edge : edges) {
            int uRoot = find(edge.u);
            int vRoot = find(edge.v);

            if (uRoot != vRoot) {
                union(uRoot, vRoot);
                totalCost += edge.w;
                components--;
                if (edge.f == 1) {
                    fEdgesInTree++;
                }
            }
        }

        long max_found = Arrays.stream(edges).filter(e -> e.f == 1).count();

        System.out.println("AA: " + fEdgesInTree + " " + max_found + " " + components + " " + totalCost);


        if (fEdgesInTree != max_found || components > 1) {
            System.out.println(-1);
        } else {
            System.out.println(totalCost);
        }
    }

    static int find(int x) {
        if (parent[x] == x) {
            return x;
        }
        return parent[x] = find(parent[x]);
    }

    static void union(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);
        parent[xRoot] = yRoot;
    }
}