package Hw5;
import java.util.Scanner;
public class AIPrerequisites {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] prerequisites = new int[n + 1][101];

        for (int i = 1; i <= n; i++) {
            int j = 0;
            int prerequisite;
            while ((prerequisite = sc.nextInt()) != 0) {
                prerequisites[i][j++] = prerequisite;
            }
            prerequisites[i][j] = 0;
        }

        int longestChain = findLongestChain(prerequisites, n);
        System.out.println(longestChain);
    }

    static int findLongestChain(int[][] prerequisites, int n) {
        int[] chainLengths = new int[n + 1];
        int maxChain = 0;

        for (int i = 1; i <= n; i++) {
            int chainLength = findChainLength(prerequisites, chainLengths, i);
            if (chainLength > maxChain) {
                maxChain = chainLength;
            }
        }

        return maxChain;
    }

    static int findChainLength(int[][] prerequisites, int[] chainLengths, int course) {
        if (chainLengths[course] != 0) {
            return chainLengths[course];
        }

        int maxPrerequisiteChain = 0;
        for (int i = 0; prerequisites[course][i] != 0; i++) {
            int prerequisite = prerequisites[course][i];
            int prerequisiteChainLength = findChainLength(prerequisites, chainLengths, prerequisite);
            if (prerequisiteChainLength > maxPrerequisiteChain) {
                maxPrerequisiteChain = prerequisiteChainLength;
            }
        }

        chainLengths[course] = maxPrerequisiteChain + 1;
        return chainLengths[course];
    }
}