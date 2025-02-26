import java.util.Scanner;

class SmallestTwo {
	Scanner input = new Scanner(System.in);

	public static int[] getSmallestTwo(int[] values) {
		int[] smallest = new int[2];

		for(int i = 0; i < values.length; i++){
			int x = values[i];

			if(smallest[0] == 0 || x < smallest[0]){
				smallest[1] = smallest[0];
				smallest[0] = x;
			}

			if(smallest[0] != x || smallest[1] != 0 && x >= smallest[1])
				smallest[1] = x;

		}

		return smallest;
	}

	public static String arrayToString(int[] values){
		String array = "";

		for(int i = 0; i < values.length; i++){
			array += values[i] + "\n";
		}

		return array;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		int lines = input.nextInt();
		int[] values = new int[lines];

		for(int i = 0; i < lines; i++){
			values[i] = input.nextInt();
		}

		int[] smallest = getSmallestTwo(values);

		System.out.print(arrayToString(smallest));

	}
}