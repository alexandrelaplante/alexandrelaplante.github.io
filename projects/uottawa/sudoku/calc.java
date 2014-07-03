import java.util.ArrayList;

public class calc {

	public static void main(String[] args) {

		
		calc aaaa = new calc();
		aaaa.go();

	}

	public void go(){
	
		
		boolean s[][] = new boolean[9][9];
		for (int i=0; i<s.length; i++){
			for (int j=0; j<s[i].length; j++){
				s[i][j] = false;
			}
		}
		

		board b = new board(s, 0);
		
		rec(b,0);

		System.out.println("nonIsomorphs6");
		for(String out : nonIsomorphs6){
			System.out.println(out);
		}
		
		System.out.println("nonIsomorphs7");
		for(String out : nonIsomorphs7){
			System.out.println(out);
		}
		
		System.out.println("nonIsomorphs8");
		for(String out : nonIsomorphs8){
			System.out.println(out);
		}
		
		System.out.println("nonIsomorphs9");
		for(String out : nonIsomorphs9){
			System.out.println(out);
		}

		
		System.out.println("nonIsomorphs6.size() = "+nonIsomorphs6.size());
		System.out.println("nonIsomorphs7.size() = "+nonIsomorphs7.size());
		System.out.println("nonIsomorphs8.size() = "+nonIsomorphs8.size());
		System.out.println("nonIsomorphs9.size() = "+nonIsomorphs9.size());
		
	}
	
	int progress = 0;
	
	
	ArrayList<String> nonIsomorphs1to5 = new ArrayList<String>();
	ArrayList<String> nonIsomorphs6 = new ArrayList<String>();
	ArrayList<String> nonIsomorphs7 = new ArrayList<String>();
	ArrayList<String> nonIsomorphs8 = new ArrayList<String>();
	ArrayList<String> nonIsomorphs9 = new ArrayList<String>();
	
	ArrayList<board> rec(board b, int depth){
		
		ArrayList<board> out = new ArrayList<board>();
		int targetDepth = 6;
		
		if (depth == targetDepth){
			
			sudoku sb = b.toSudoku();
			sb.transpose();
			if (b.enoughRows(9-depth) && sb.isCanon()){ //discard impossible ones

				//Prints out the 6 hint boards in order to show progress.
				System.out.println("++++++++++");
				System.out.println("number "+nonIsomorphs6.size());
				System.out.println(sb);
				String sbs = sb.toBin();
				
				if(!nonIsomorphs6.contains(sbs)){
					nonIsomorphs6.add(sbs);
					
					for (board n : b.nextPiece()){
						sudoku sn = n.toSudoku();
						sn.transpose();
						if (n.enoughRows(2) && sn.isCanon()){
							String sns = sn.toBin();
							if(!nonIsomorphs7.contains(sns)){
								nonIsomorphs7.add(sns);
								for (board nn : n.nextPiece()){
									sudoku snn = nn.toSudoku();
									snn.transpose();
									if (nn.enoughRows(1) && snn.isCanon()){
										String snns = snn.toBin();
										if(!nonIsomorphs8.contains(snns)){
											nonIsomorphs8.add(snns);
											
											for (board nnn : nn.nextPiece()){
												sudoku snnn = nnn.toSudoku();
												snnn.transpose();
												if (nnn.enoughRows(0) && snnn.isCanon()){
													String snnns = snnn.toBin();
													if(!nonIsomorphs9.contains(snnns)){
														nonIsomorphs9.add(snnns);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				
				}
			}
			return out;
			}
		else{

			if (b.enoughRows(9-depth)  ){ //discard impossible ones
				ArrayList<board> bs = new ArrayList<board>();
				bs = b.nextColumn();
				for (board bo : bs){
					out.addAll(rec(bo, depth+1));
				}
			}
			return out;
		}
	}
	
	public class board{
		//b symbolizes how many rows have elements
		boolean[] booard = new boolean[9];
		boolean[][] sud;
		int depth;
		
		board(boolean[] a){
			booard=a;
		}
		
		sudoku toSudoku(){
			boolean[][] ss = new boolean[9][9];
			//turn sud into ss
			for (int i=0; i<9; i++){
				for (int j=0; j<9; j++){
					ss[i][j] = sud[i][j]; //the rest are false
				}	
			}
			sudoku output = new sudoku(ss);
			return output;
		}
		
		board(boolean[][] a, int d){
			sud = a;
			for (int i=0; i<9; i++){
				boolean hasPiece = false;
				for (int j=0; j<9; j++){
					if (a[j][i]==true){ hasPiece = true; }
				}	
				booard[i] = hasPiece;
			}
			depth = d;
		}
		
		
		public ArrayList<board> nextColumn(){
			ArrayList<board> out = new ArrayList<board>();
			/*
			 * the next column can have the hint
			 * in any of 8 rows, but wlog some
			 * are the same.
			 */ 
			out.addAll(ThreeBlock(booard[0],booard[1],booard[2],
					booard[3],booard[4],booard[5],
					booard[6],booard[7],booard[8],
					0, booard, sud));
			return out;
		}
		
		public boolean enoughRows(int hints){
			// returns false if this board can never be completed with these hints.
			// if there are too many empty rows we can't finish the hints without
			// having empty 2 rows in a block.
			// empty rows <= 3 in a solvable sudoku
			int needs1 = 2;
			int needs2 = 2;
			int needs3 = 2;
			for (int i=0; i<8; i++){
				if(i<3 && booard[i]){ needs1--; }
				else if(i<6 && booard[i]){ needs2--; }
				else if(booard[i]){ needs3--; }
			}
			int totalHintsNeeded = 0;
			if (needs1 > 0){ totalHintsNeeded+= needs1; }
			if (needs2 > 0){ totalHintsNeeded+= needs2; }
			if (needs3 > 0){ totalHintsNeeded+= needs3; }
			return totalHintsNeeded <= hints;
		}
		
		public ArrayList<board> nextPiece(){
			ArrayList<board> out = new ArrayList<board>();
			
			for (int i=0; i<9; i++){
				for (int j=0; j<9; j++){
					if (!sud[i][j]){
						//copy sud
						boolean sudsud[][] = new boolean[9][9];
						for (int ii=0; ii<sud.length; ii++){
							for (int jj=0; jj<sud[ii].length; jj++){
								sudsud[ii][jj] = sud[ii][jj];
							}
						}
						sudsud[i][j] = true;
						out.add(new board(sudsud, -1));
					}
				}
			}
			
			return out;
			
		}
		
		public String toString(){
			String o = "";
			int n = 1;
			for (int i=0; i<sud.length; i++){
				for (int j=0; j<sud.length; j++){
					if (sud[j][i]){
						o+="1";
					} else {
						o+=".";
					}
				if (j==2 || j==5){
					o+="|";
				}
					
				}
				if (i<9){ o+= " "+booard[i]; }
				o+= "\n";
			}
			return o;
		}
		
		//at an end node, create a next board with
		//a new elem in the top position
		ArrayList<board> TwoRow(boolean b1,boolean b2, int pos, boolean[] b, boolean[][] s){
			//if top element is taken, split into row1 row2
			//else end node
			ArrayList<board> o = new ArrayList<board>();
			o.addAll(Row(b1, pos, b, s));
			if (b1){
				o.addAll(Row(b2, pos+1, b, s));
			}
			return o;
		}
		
		ArrayList<board> ThreeBlock(boolean b1, boolean b2, boolean b3,
				boolean b4, boolean b5, boolean b6,
				boolean b7, boolean b8, boolean b9,
				 int pos, boolean[] b, boolean[][] s){
		//if top block has something, split into Block1 Twoblock2
		//else end node
		ArrayList<board> o = new ArrayList<board>();
		o.addAll(Block(b1,b2,b3, pos, b, s));
		if (b1){
			o.addAll(TwoBlock(b4,b5,b6,b7,b8,b9, pos+3, b, s));
		}
		return o;
	}
		
		ArrayList<board> TwoBlock(boolean b1, boolean b2, boolean b3,
					 boolean b4, boolean b5, boolean b6,
					 int pos, boolean[] b, boolean[][] s){
			//if top block has something, split into block1 block2
			//else end node
			ArrayList<board> o = new ArrayList<board>();
			o.addAll(Block(b1,b2,b3, pos, b, s));
			if (b1){
				o.addAll(Block(b4,b5,b6, pos+3, b, s));
			}
			return o;
		}
		
		ArrayList<board> Block(boolean b1, boolean b2, boolean b3,
				int pos, boolean[] b, boolean[][] s){
			//if top element is taken, split into row1 TwoRow1
			//else end node
			ArrayList<board> o = new ArrayList<board>();
			o.addAll(Row(b1, pos, b, s));
			if (b1){
				o.addAll(TwoRow(b2,b3, pos+1, b, s));
			}
			return o;
		}
		
		ArrayList<board> Row(boolean b1, int pos, boolean[] b, boolean[][] s){
			//end node
			ArrayList<board> o = new ArrayList<board>();
			boolean sudsud[][] = new boolean[9][9];
			for (int i=0; i<s.length; i++){
				for (int j=0; j<s[i].length; j++){
					sudsud[i][j] = s[i][j];
				}
			}
			sudsud[depth][pos] = true;
			int d = depth+1;
			if (d==2 || d==5) { d++; }
			o.add(new board(sudsud, d));
			return o;
		}
		
	}
}

