import java.util.Scanner;

class Reservoir {
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

	public static int getBiggestWaterArea(int n, int[] heights){
		int area = 0; //dam area
		int biggestIndex = 0; //index of peak wall
		int ground = 0; //current lowest wall
		int biggestWaterArea = 0;
		boolean filling = false; 

		for(int i = 0; i < n; i++){

			int biggest = heights[biggestIndex]; //highest wall peak
			int num = heights[i];

			if(!filling){ //if water shouldnt be filling this area
				if(num < biggest){
					filling = true;
					ground = num;
				} else {
					biggestIndex = i;
					area = 0;
				}
			} 
			if (filling) {
				if( num > ground ) {

					int lowerWall = (num > biggest)?biggest:num; //the lower wall between two end points
					int x = i - biggestIndex - 1; // x = amount of spaces between two end points
					
					int waterArea = lowerWall * x - area; //area of water filled between two end points

					if(waterArea > biggestWaterArea){
						biggestWaterArea = waterArea;
					}

					if ( num >= biggest ) {
						filling = false;
						biggestIndex = i;
						biggest = num;
						area = -num;

					} else {
						ground = num;
					}

				}

				area += num;
			}

		}

		return biggestWaterArea;


	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		int n = input.nextInt();
		int[] heights = new int[n];
		int[] heightsR = new int[n];

		for(int i = 0; i < n; i++){
			int nextHeight = input.nextInt();
			heights[i] = nextHeight;
			heightsR[n - i - 1] = nextHeight;
		}

		
		int biggestWaterAreaL = getBiggestWaterArea(n, heights);
		int biggestWaterAreaR = getBiggestWaterArea(n, heightsR);

		log(biggestWaterAreaR>biggestWaterAreaL?biggestWaterAreaR:biggestWaterAreaL,true);

	}
}

/*
- area = total after biggest
- water area = biggest * (i - 1) - area
- when next ingeter is smaller water can start to be filled
- store that bigger integer as current biggest
- store smaller integer as ground
- when next integer is bigger than smaller
	- if water area is biggest
		- store water area
	- if is bigger than current biggest
		- store as biggest
	
*/
