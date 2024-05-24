import java.util.Scanner;

class Planters {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		//Get Input into Arrays
		int p = input.nextInt();
		int r = input.nextInt();

		int[] s = new int[p]; //plants in planters
		int[] t = new int[r]; //extra planters

		for(int i = 0; i < p; i++){
			s[i] = input.nextInt();
		}

		for(int i = 0; i < r; i++){
			t[i] = input.nextInt();
		}
		
		ArrayFuncs array = new ArrayFuncs();

		//Sort Arrays
		array.set(s);
		s = array.sort("mergeL"); //mergeSort Largest->smallest

		array.set(t);
		t = array.sort("mergeL");

		//Combine Arrays to build list of planters
		t = array.merge(s, t, true);

		//compare size of plants to planters
		boolean possible = true;

		for(int i = 0; i < s.length; i++){
			if ( t[i] <= s[i] )
				{ possible = false; break;}
		}

		System.out.print(possible?"YES":"NO");

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

	public int[] merge(int[] half1, int[] half2, boolean byLarger){
		int i=0, j=0, k=0;

		int[] output = new int[half1.length + half2.length];

		while (i < half1.length && j < half2.length){

			if (half1[i] <= half2[j] && !byLarger || half1[i] >= half2[j])
				{ output[k] = half1[i]; i++; }
			else
				{ output[k] = half2[j]; j++; }
			k++;
		}

		while(i < half1.length)
			{ output[k] = half1[i]; i++; k++; }

		while(j < half2.length)
			{ output[k] = half2[j]; j++; k++; }

		return output;
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

