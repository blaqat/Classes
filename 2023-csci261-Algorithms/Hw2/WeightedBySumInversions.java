import java.util.Scanner;

class WeightedBySumInversions {

	public static int sumUntil(int[] array, int num){
		int s = 0; //sum of numbers inverted with
		int c = 0; //count of inversions

		boolean f = false; //found number

		for(int i = 0; i < array.length; i++){
			int ai = array[i];

			if(ai == num){
				f = true;
			}

			if(!f){
				s += ai;
				c++;
			}
			else { //shift numbers afterwards until end
				if(i+1 < array.length && array[i+1] != -1){
					array[i] = array[i+1];
					array[i+1] = -1;
				}

			}
		}

		return f?s + num*c:0;
	}
	public static void log(Object a, boolean nl){
		String log = "" + a;
		if(nl)
			System.out.println(log);
		else 
			System.out.print(log);
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int l = input.nextInt();
		int[] sequence = new int[l];
		for(int i = 0; i < l; i++){
			sequence[i] = input.nextInt();
		}

		int[] sorted = new ArrayFuncs(sequence).sort("mergeS");
		
		int weightedCount = 0;

		for(int op = 0; op < l; op++){
			int n = sequence[op];
			int weight = sumUntil(sorted, n);
			weightedCount += weight;
		}

		log(weightedCount, true);
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
			if (!byLarger && half1[i] <= half2[j] || byLarger && half1[i] >= half2[j])
				{ 
					output[k] = half1[i]; i++;
				}
			else
				{ 
					output[k] = half2[j];  
					j++;
				}
			k++;
		}

		while(i < half1.length)
			{ output[k] = half1[i]; i++; k++;}

		while(j < half2.length)
			{ output[k] = half2[j]; j++; k++;}

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
