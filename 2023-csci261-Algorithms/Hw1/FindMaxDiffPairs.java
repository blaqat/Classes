import java.util.Scanner;


class FindMaxDiffPairs {

	public static void log(Object a, boolean nl){
		String log = "" + a;
		if(nl)
			System.out.println(log);
		else 
			System.out.print(log);
	}

	public static void log_pair(int a, int b, boolean nl){
		String log = "(" + a + ", " + b + ")";
		if(nl)
			System.out.println(log);
		else 
			System.out.print(log);
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		ArrayFuncs array = new ArrayFuncs();

		//int[] test = {2,7,3,1,5,6};
		//int[] test = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
		//int[] test = {4856,-6292,-9201,-9566,3537,463,-1584,-6666,2868,4860,4760,1635,-5544,-9002,-7890,-1050,8112,7588,2899,-9505,-9156,-6322,-6340,4085,7830,313,8289,7093,2348,-5123,-5325,-7575,-2509,-8749,-965,-2621,3606,-8969,-34,4800,4619,1378,-3386,-4799,-4772,-5579,4359,4582,-1249,-6764,9039,-443,3012,-8984,-2848,3213,-8519,-5773,3171,8954,-483,194,-7892,7613,2167,979,2356,-6492,3015,342,-8212,-7508,7261,2709,-2399,9389,-3414,3880,4911,1449,2896,8837,2965,730,-1240,8149,-6707,4719,-1192,-8466,-804,-3587,-8947,-4075,-9108,-5962,9809,40,5664,9307};
		int size = input.nextInt();
		int[] a = new int[size];
		int[] diffs = new int[a.length*a.length];
		int[] counts = new int[diffs.length];
		int dii = 0;
		for(int i = 0; i < size; i++){
			a[i] = input.nextInt();
		}
		array.set(a);
		a = array.sort("mergeL");

		for (int i = 0; i < a.length - 1; i++) {
			for(int j = 0; j < a.length - 1; j++){
				if(i != j){
					int t = Math.abs(a[i] - a[j]);
					//log_pair(a[i], a[j], true);
					boolean f = false;
					for(int di = 0; di < dii; di++){
						int d = diffs[di];
						if(d==t){
							//log("\t" + t, true);
							f = true;
							counts[di]++;
						}
					}
					if(!f){
						//log("\t=>" + t, true);
						diffs[dii]=t;
						dii++;
					}

				}
			}
		}

		int max = 0;
		int maxi = 0;
		for(int i = 0; i < dii; i++){
			//log_pair(counts[i], diffs[i], true);
			int c = counts[i];
			if(c > max){
				max = c;
				maxi = i;
			}
		}

		log(diffs[maxi], true);

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

