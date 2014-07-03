
import java.util.TreeSet;
import java.io.*;
import java.util.Iterator;

public class set {
	static int MAXSfromfile;
	static int MAXS = 10000;
	
	static long Idef = 2;

	//S[x][y] == true if S_x contains y
	boolean S[][];

	static int m[] = {1, 169, 289, 361, 529, 841,
		   961,  1369, 1681, 1849, 2041, 2209,
		   2521, 2641, 2689, 2809, 3361, 3481,
		   3529, 3721, 4321, 4489, 5041, 5161,
		   5329, 5569, 6169, 6241, 7561, 7681,
		   7921, 8089, 8761};
	
	static int skipAmount[] ={168,120,72,168,312,120,408,312,168,192,168,312,120,48,120,552,120,
		48,192,600,168,552,120,168,240,600,72,1320,120,240,168,672,480};

	public static void main(String[] args) throws InterruptedException {
		
		if (args.length > 0){ Idef = Long.parseLong(args[0]); }

		set oooo = new set();
		oooo.start();

	}

	public void start() throws InterruptedException{


		try{
			/* Read in the file of filters,
			 * you must generate this file first.
			 */
			FileReader fr = new FileReader("filters.txt"); 
			BufferedReader br = new BufferedReader(fr); 
			String line;
			int i = 0;
			boolean firstLine = true;
			while((line = br.readLine()) != null) {
				if (firstLine){
					System.out.println("Loading the S filters... "+line);
					MAXSfromfile = Integer.parseInt(line.substring(5));
					S = new boolean[MAXSfromfile][4*MAXSfromfile];
					firstLine = false;
				}
				String[] Js;
				Js = line.split(" ");
				for (int j = 1; j<Js.length - 1; j++){
					S[i][Integer.parseInt(Js[j])] = true;
				}
				i++;
			} 
			fr.close(); 
			}catch (IOException e){
			System.err.println("Error: " + e);
		}
			/***********************************
			 * * * * * S t a r t i n g * * * * *
			 ***********************************/
			System.out.println("starting");
			
			tuple t = calc(Idef);
			//t = mixWith9240(t);
			//System.out.println("x (mod "+t.M+") = "+ t.set);
		
			try{
				// Create file 
				FileWriter fstream = new FileWriter("out.txt");
				BufferedWriter out = new BufferedWriter(fstream);

				out.write("x (mod \n"+t.M+"\n) = "+ t.set+"\n");
				//Close the output stream
				out.close();
				}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
			
		int count = t.set.size();
		float p = ((float)count/(float)t.M) * 100;
		System.out.println("The amount that pass through is "+count+"/"+t.M +"= "+p+"%");
		
	}
	class tuple{
		public TreeSet<Long> set;
		public long M;
		tuple(TreeSet<Long> s, long m){
			set = s;
			M = m;
		}
	}
	
	boolean first = true;
	public tuple calc(long I) {


		TreeSet<Long> Set = new TreeSet<Long>();
		long M;
		if (I == 1){
			M = 3;
			Set.add(1L);
			
		} else if (I == 5 && first){
			first = false;
			tuple t = calc(I);
			t = mixWith9240(t);
			Set = t.set;
			M = t.M;
		} else {
			//if (I > 5){I = I - 1;}
			long i = I; //not needed
			tuple old = calc(I - 1);
			
			M = lcm(old.M, 4*i -1);
			System.out.println("I = "+ I);
			System.out.println("old.M = "+old.M);
			System.out.println("4i-1 = "+(4*i -1));
			System.out.println("M = "+M);
			System.out.println();
			
			TreeSet<Long> N1 = new TreeSet<Long>();
			for (long x : old.set){
				long m = 0;
				while(m < M){
					N1.add(x + m);
					m += old.M;
				}
			}
			
			TreeSet<Long> N2 = new TreeSet<Long>();
			Iterator it = N1.iterator();
			while (it.hasNext()){
				long x = (Long)it.next();
				if (S[(int)i][(int)(x%(4*i - 1))]){ //if s%4i-1 is in S, ie: covered by S(i)
					it.remove();
				}
			}
			
			Set = N1;
			//System.out.println("oldSet = "+oldSet);
			//System.out.println("outerSet = " +outerSet);
			//System.out.println("newSet = " +newSet);
			
			//outerSet = intersection(outerSet, newSet);
			
			//System.out.println("result = "+outerSet);
			//System.out.println("");
			
		}
		
		return new tuple(Set, M);


	}
		
//		for (long j = 0; j <= M; j++){ //for every i<S
//			outerSet.add(j);
//		}
//		for (long i = 1; i <= I; i++){ //for every i<S
//			
//			TreeSet<Long> middleSet = new TreeSet<Long>();
//			
//			for (long s = 0; s < (4*i - 1); s++){
//				if (!S[(int)i][(int)s]){ //for every s not in S(i)
//					
//					
//					TreeSet<Long> innerSet = new TreeSet<Long>();
//					for (long n = 0; n <= (M/(4*i - 1)) -1; n++){
//						long newNum = s + (4*i -1)*n;
//						//for (int j = 0; j<33; j++){
//						//	if (newNum%9240==m[j]){
//								innerSet.add(newNum);
//						//	}
//						//}
//						
//					}
//					//System.out.println("innerSet: "+ innerSet);
//					middleSet.addAll(innerSet);
//				}
//				
//			}
//			//System.out.println("middleSet: "+ middleSet);
//			outerSet = intersection(outerSet, middleSet);
//
//		}
//		return outerSet;
//	}
	
	public tuple mixWith9240(tuple old){
		
		long M = lcm(old.M, 9240);
		TreeSet<Long> outerSet = new TreeSet<Long>();
		for (long x : old.set){
			long m = 0;
			while(m < M){
				outerSet.add(x + m);
				m += old.M;
			}
		}
		
		TreeSet<Long> newSet = new TreeSet<Long>();
		int skipIndex = -1;
		for (long s = 1; s < 9240; s+= skipAmount[skipIndex]){
			for (long n = 0; n <= (M/9240) -1; n++){
				long newNum = s + (9240)*n;
				newSet.add(newNum);
			}
			skipIndex = (skipIndex+1) % 33;
		}
		
		return new tuple(newSet, M);
	}

//	public static <T> TreeSet<T> intersection(TreeSet<T> setA, TreeSet<T> setB) {
//	    TreeSet<T> tmp = new TreeSet<T>();
//	    for (T x : setA)
//	      if (setB.contains(x))
//	        tmp.add(x);
//	    return tmp;
//}
	
	public static long lcm(long a, long b){
		return a*b / gcd(a,b);
	}
	
	public static long gcd(long a, long b){
		if (b==0){
			return a;
		} else {
			return gcd(b, a%b);
		}
	}

}
