import java.util.Scanner;

class AllWhiteSquare {
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

	public static int get_biggest_square(int s, int sy, int sx, boolean[][] grid){
		int max_s = s-1;

		if(s + sy >= grid.length || s + sx >= grid.length)
			return max_s;

		for(int y = sy; y<=sy+s; y++){
			if(!grid[y][sx+s]){				
				return max_s;
			}				
		}

		for(int x = sx; x<=sx+s; x++){
			if(!grid[sy+s][x]){				
				return max_s;
			}
		}

		max_s = get_biggest_square(s + 1, sy, sx, grid);

		return Math.max(max_s, s);
	}

	
	public static int biggest_all_white(int n, boolean[][] grid){
		int max_white = 0;

		for(int y = 0; y < n-1; y++){
			for(int x = 0; x < n-1; x++){
				if(grid[y][x]){
					max_white = Math.max(max_white, get_biggest_square(1, y, x, grid));
				}
			}
		}


		return max_white + 1;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		int n = input.nextInt();
		boolean[][] grid = new boolean[n][n];

		input.nextLine();
		for(int y = 0; y < n; y++){
			String row = input.nextLine();
			for(int x = 0; x < n; x++){
				grid[y][x] = row.charAt(x) == 'w';
			}
		}


		log(biggest_all_white(n, grid), true);

	}
}