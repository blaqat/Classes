package Hw6;

import java.util.Scanner;

public class SpanTree {
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
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int m = input.nextInt();
        int[] G = new int[n + 1];
        int[][] edges = new int[m][4];
        ArrayFuncs arrays = new ArrayFuncs(G);

        for(int i = 0; i < m; i++){
            int[] edge = {
                    input.nextInt(), 
                    input.nextInt(), 
                    input.nextInt(), 
                    input.nextInt()
            };

            edges[i] = edge;
        }

        //Merge Sort
        edges = arrays.special_sort(edges, "edgeGraph");

        for(int i = 0; i <= n; i++){
            G[i] = i;
        }

        int min = 0;
        int min_found = 0;
        int max_found = 0;
        int c = n;

        for(int[] edge : edges){
            int x = arrays.find(edge[0]);
            int y = arrays.find(edge[1]);
            if(edge[3] == 1)
                max_found++;

            if(x != y){
                if(edge[3] == 1)
                    min_found++;

                arrays.union(x, y);
                min += edge[2];
                c--;
            }
        }

        if(min_found != max_found || c > 1)
            log(-1, true);
        else
            log(min, true);
    }
    
}
class ArrayFuncs {
	private int[] array;

	public ArrayFuncs(){}

	public ArrayFuncs(int[] array){
		this.set(array);
	}
	public int[] get(){
		return this.array;
	}

	public void set(int[] array){
		this.array = array;
	}
	public String toString(){
		String s = "Array {";

		for(int n : this.array){
			s += n + ",";
		}

		s += "}";

		return s;
	}

	public void log(){
		System.out.println(this);
	}

	public int[] sort(String type){
		int[] output;

		switch(type){
			case "mergeS": 
				output = mergeSort(this.array, this.array.length, false);
				break;

			case "mergeL": 
				output = mergeSort(this.array, this.array.length, true);
				break;

			default:
				output = this.array;
		}

		this.array=output;

		return output;
	}

    public int[][] special_sort(int[][] matrix, String type){
		int[][] output;

		switch(type){
			case "edgeGraph": 
				output = mergeSortEdge(matrix, 0, matrix.length - 1);
				break;

			default:
				output = matrix;
		}

		return output;
	}

    public int find(int x) {
        if (this.array[x] == x) {
            return x;
        }
        return this.array[x] = find(this.array[x]);
    }

    public void union(int x, int y) {
        int final_x = this.find(x);
        int final_y = this.find(y);
        this.array[final_x] = final_y;
    }

	public int[][] split(int[] input, int index){
		int c = 0;
		int[] half1 = new int[index];
		int[] half2 = new int[input.length-index];

		for(int i = 0; i < input.length; i++){
			if(i < index)
				half1[i] = input[i];
			else
				{ half2[c] = input[i]; c++; }
		}

		return new int[][] {half1, half2};
	}

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

    public static int[][] mergeEdge(int[][] edges, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
    
        int[][] L = new int[n1][4];
        int[][] R = new int[n2][4];
    
        for (int i = 0; i < n1; ++i) {
            L[i] = edges[left + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = edges[middle + 1 + j];
        }
    
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i][3] != R[j][3] ? L[i][3] > R[j][3] : L[i][2] < R[j][2]) {
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

        return edges;
    }
    
    public static int[][] mergeSortEdge(int[][] edges, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
    
            mergeSortEdge(edges, left, middle);
            mergeSortEdge(edges, middle + 1, right);
    
            return mergeEdge(edges, left, middle, right);
        }
        
        return edges;
    }

	private int[] mergeSort(int[] input, int n, boolean byLarger){
		if (n<2) return input;

		int[][] split = this.split(input, n/2);

		int[] half1 = split[0];
		int[] half2 = split[1];


		int[] sortedHalf1 = this.mergeSort(half1, n/2, byLarger);
		int[] sortedHalf2 = this.mergeSort(half2, n-n/2, byLarger);

		
		return this.merge(sortedHalf1, sortedHalf2, byLarger);
	}
}