import java.util.ArrayList;
import java.util.Arrays;



public class sudoku {

	boolean[][] sudo = new boolean[9][9];
	grid s = new grid(sudo);
	
	double score = -1;
	String bin = "-1";
	
	/*
	 * s[i][j] is an entry.
	 * s[i]    is a row.
	 * 
	 * s[2][8]
	 * ...|...|...
	 * ...|...|...
	 * ...|...|..X
	 * ...|...|...
	 * ...|...|...
	 * ...|...|...
	 * ...|...|...
	 * ...|...|...
	 * ...|...|...
	 *  
	 */
	
	
	class grid{
		boolean[][] ss = new boolean[9][9];
		boolean transpose = false;
		
		
		public grid(boolean[][] sss){
			ss = sss;
		}
		
		public boolean get(int x, int y){
			if (transpose){
				return ss[y][x];
			} else {
				return ss[x][y];
			}
		}
		
		public void set(int x, int y, boolean b){
			if (transpose){
				ss[y][x] = b;
			} else {
				ss[x][y] = b;
			}
		}
		
		public grid clone(){
			boolean[][] copy = new boolean[9][9];
			for (int i=0; i<9; i++){
				for (int j=0; j<9; j++){
					copy[i][j] = ss[i][j];
				}	
			}
			grid out = new grid(copy);
			out.transpose = transpose;
			
			return out;
		}
		
		
		public void transpose(){
			transpose = !transpose;
		}
	}
	
	public sudoku(boolean[][] inss){
		//s = ss;
		for (int i=0;i<inss.length;i++){
			s.ss[i] = inss[i].clone();
		}
	}
	
	public sudoku(grid in){
		s = in.clone();
	}
	
	
	static int perm[][] = { {0,1,2},
							{0,2,1},
							{1,0,2},
							{1,2,0},
							{2,0,1},
							{2,1,0},
						  };
	
	
	
	static class tuple{
		sudoku sudoku;
		boolean bool;
		tuple(sudoku su, boolean boo){
			sudoku = su;
			bool = boo;
		}
	}
	
	static public tuple recIsCanon(sudoku b, int depth, String original){
		//for every possible ordering, pursue the ones that are ordered
		
		if( check[depth].equals("base case")){
			b.bin = b.toBin();
			boolean better = (b.bin.compareTo(original) > 0); //any score larger than our original means we weren't canonical
			return new tuple(b, !better); //if it's better we want false.
		}
		ArrayList<tuple> pursue = new ArrayList<tuple>();
		for(int i=0; i<6; i++){
			if(inOrder(perm[i], b, depth)){
				tuple t = recIsCanon(changeOrder(i, b, depth), depth+1, original);
				if (t.bool == false){ return t; } //save time by immediately returning false
				pursue.add( t );
			}
		}
		
		String best = "-1";
		tuple theBest = null;
		for (tuple t : pursue){
			if (t.sudoku.bin.compareTo(best) > 0){
				best = t.sudoku.bin;
				theBest = t;
			}
		}

		return theBest;
		
	}
	
	static String check[] = {"block row", "rows 1", "rows 2","rows 3", "block col",
		"cols 1","cols 2","cols 3","base case"};
	
	static sudoku changeOrder(int order, sudoku b, int d) {
		//012 means don't change
		//021 means swap(1,2)
		//102 means swap(0,1)
		//120 means swap(0,1) swap(1,2)
		//201 means swap(0,2) swap(1,2)
		//210 means swap(0,2)
		sudoku newB = new sudoku(b.s);
		if (order==0){
			//012 means don't change
		} else if (order==1){
			newB = swap(1,2, newB, d);
		} else if (order==2){
			newB = swap(0,1, newB, d);
		} else if (order==3){
			newB = swap(0,1, newB, d);
			newB = swap(1,2, newB, d);
		} else if (order==4){
			newB = swap(0,2, newB, d);
			newB = swap(1,2, newB, d);
		} else if (order==5){
			newB = swap(0,2, newB, d);
		}
		return newB;
	}
	
	static sudoku swap(int a, int b, sudoku sud, int d) {
		if (check[d].equals("block row")){
			sud.swapBlockRows(a, b);
		} else if (check[d].equals("block col")){
			sud.swapBlockCols(a, b);
		} else if (check[d].equals("rows 1")){
			sud.swapRows(a, b);
		} else if (check[d].equals("rows 2")){
			sud.swapRows(a+3, b+3);
		} else if (check[d].equals("rows 3")){
			sud.swapRows(a+6, b+6);
		} else if (check[d].equals("cols 1")){
			sud.swapCols(a, b);
		} else if (check[d].equals("cols 2")){
			sud.swapCols(a+3, b+3);
		} else if (check[d].equals("cols 3")){
			sud.swapCols(a+6, b+6);
		}
		return sud;
	}

	static boolean inOrder(int[] order, sudoku b, int d) {
		if (check[d].equals("block row")){
			if (b.countElementsInBlockRow(order[0]) >= b.countElementsInBlockRow(order[1])
			&& b.countElementsInBlockRow(order[1]) >= b.countElementsInBlockRow(order[2])){
				return true;
			}
		} else if (check[d].equals("block col")){
			if (b.countElementsInBlockCol(order[0]) >= b.countElementsInBlockCol(order[1])
					&& b.countElementsInBlockCol(order[1]) >= b.countElementsInBlockCol(order[2])){
						return true;
			}
		} else if (check[d].equals("rows 1")){
			if (b.countElementsInRow(order[0]) >= b.countElementsInRow(order[1])
					&& b.countElementsInRow(order[1]) >= b.countElementsInRow(order[2])){
						return true;
			}
		} else if (check[d].equals("rows 2")){
			if (b.countElementsInRow(order[0]+3) >= b.countElementsInRow(order[1]+3)
					&& b.countElementsInRow(order[1]+3) >= b.countElementsInRow(order[2]+3)){
						return true;
			}
		} else if (check[d].equals("rows 3")){
			if (b.countElementsInRow(order[0]+6) >= b.countElementsInRow(order[1]+6)
					&& b.countElementsInRow(order[1]+6) >= b.countElementsInRow(order[2]+6)){
						return true;
			}
		} else if (check[d].equals("cols 1")){
			if (b.countElementsInCol(order[0]) >= b.countElementsInCol(order[1])
					&& b.countElementsInCol(order[1]) >= b.countElementsInCol(order[2])){
						return true;
			}
		} else if (check[d].equals("cols 2")){
			if (b.countElementsInCol(order[0]+3) >= b.countElementsInCol(order[1]+3)
					&& b.countElementsInCol(order[1]+3) >= b.countElementsInCol(order[2]+3)){
						return true;
			}
		} else if (check[d].equals("cols 3")){
			if (b.countElementsInCol(order[0]+6) >= b.countElementsInCol(order[1]+6)
					&& b.countElementsInCol(order[1]+6) >= b.countElementsInCol(order[2]+6)){
						return true;
			}
		}
		
		return false;
	}
	

	public boolean isCanon(){
		sudoku t1, t2;
		t1 = new sudoku(s);
		tuple tu1 = recIsCanon(t1, 0, t1.toBin());
		t2 = new sudoku(s);
		t2.transpose();
		tuple tu2 = recIsCanon(t2, 0, t2.toBin());
		if (tu1.sudoku.bin.compareTo(tu2.sudoku.bin) >= 0){
			return tu1.bool;
		} else {
			return false; //it wasn't canonical
		}
	}
	
	void transpose(){
		/*
		boolean[][] temp = new boolean[9][9];
		for (int r=0;r<s.length;r++){
			for (int c=0;c<s.length;c++){
				temp[c][r] = s[r][c];
			}			
		}
		s=temp;
		*/
		s.transpose();
	}
	
	void swapBlockCols(int a, int b){
		sudoku temp = new sudoku(s);
		temp.transpose();
		temp.swapBlockRows(a, b);
		temp.transpose();
		s = temp.s;
	}
	
	void swapBlockRows(int a, int b){
		boolean[][] tempA = new boolean[3][9];
		boolean[][] tempB = new boolean[3][9];
		
		int minRow = 3*a;
		int maxRow = minRow + 3;
		for(int i=minRow; i<maxRow; i++){
			for(int j=0; j<9; j++){
				tempA[i - minRow][j] = s.get(i, j);
			}
		}
		
		minRow = 3*b;
		maxRow = minRow + 3;
		for(int i=minRow; i<maxRow; i++){
			for(int j=0; j<9; j++){
				tempB[i - minRow][j] = s.get(i,j);
			}
		}
		
		minRow = 3*a;
		maxRow = minRow + 3;
		for(int i=minRow; i<maxRow; i++){
			for(int j=0; j<9; j++){
				s.set(i, j, tempB[i-minRow][j]);
			}
		}
		
		minRow = 3*b;
		maxRow = minRow + 3;
		for(int i=minRow; i<maxRow; i++){
			for(int j=0; j<9; j++){
				s.set(i, j, tempA[i-minRow][j]);
			}
		}
	}
	int countElementsInBlockCol(int c){

		/* The rows are already in their proper placement, we just
		 * need to place the cols.
		 * 
		 * The ones with the most elements higher up go first.
		*/
		int col1 = countElementsInCol(3*c);
		int col2 = countElementsInCol(3*c + 1);
		int col3 = countElementsInCol(3*c + 2);
		int cols[] = {col1, col2, col3};
		Arrays.sort(cols);
		int score = 0;
		score += cols[2]*100000;
		score += cols[1]*1000;
		score += cols[0]*1;

		return score;
		
	}
	int countElementsInBlockRow(int c){

		/*
		 * score works like this:
		 * number of elements in most populated row * 100
		 * number of elements in second most * 10
		 * number of elements in least *1
		 * this way we only care which rows have most guys, not the entire block
		 * row.
		 */
		int row1 = countElementsInRow(3*c);
		int row2 = countElementsInRow(3*c + 1);
		int row3 = countElementsInRow(3*c + 2);
		int rows[] = {row1, row2, row3};
		Arrays.sort(rows);
		int score = 0;
		score += rows[2]*100;
		score += rows[1]*10;
		score += rows[0]*1;

		return score;
	}
	
	void swapRows(int a, int b){
		boolean temp[] = new boolean[9];
		for (int i=0;i<9;i++){
			temp[i] = s.get(a, i);
		}
		//temp = s[a].clone();
		for (int i=0;i<9;i++){
			s.set(a, i, s.get(b, i));
		}
		//s[a] = s[b].clone();
		for (int i=0;i<9;i++){
			s.set(b, i, temp[i]);
		}
		//s[b] = temp;
	}
	
	void swapCols(int a, int b){
		transpose();
		swapRows(a,b);
		transpose();
	}
	
	int countElementsInRow(int a){
		int count = 0;
		for(int i=0; i<9; i++){
			if(s.get(a,i)){ count++; }
		}
		return count;
	}
	
	int countElementsInCol(int a){

		//score cols based on how many pieces are highest up
		int score = 0;
		for(int i=0; i<9; i++){
			if (s.get(i,a)) {score += Math.pow(2,(9-i)-1); } //higher=better
		}
		return score;
	}
	

	public String toBin(){
		String out = "";
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				if (s.get(i,j)){
					out+= "1";
				} else {
					out+="0";
				}
			}	
		}
		return out;
	}
	
	public String toString(){
		String o = "";
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				if (s.get(i,j)){
					//o+=n; n++;
					o+="1";
				} else {
					o+=".";
				}
			if (j==2 || j==5){
				o+="|";
			}
				
			}
			o+= "\n";
		}
		return o;
	}
}