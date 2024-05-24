package Hw5;

import java.util.Scanner;

public class Prerequisites {
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

    public static int get_length(int[][] P, int[] lens, int req){
        if(lens[req] != 0){
            return lens[req];
        }

        int max_len = 0;
        
        for(int i = 0; P[req][i] != 0; i++){
            int prereq = P[req][i];
            max_len = Math.max(max_len, get_length(P, lens, prereq));
        }

        return lens[req] = max_len + 1;
    }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        int n = input.nextInt();
        int[][] P = new int[n+1][101];
        int[] lengths = new int[n+1];

        for(int i = 1; i <= n; i++){
            int j = 0;
            int prereq = input.nextInt();

            while(prereq != 0){
                P[i][j] = prereq;
                prereq = input.nextInt();
                j++;
            }
        }

        int max_length = 0;

        for(int i = 1; i <= n; i++){
            max_length = Math.max(max_length, get_length(P, lengths, i));
        }

        log(max_length, true);
    }
}
