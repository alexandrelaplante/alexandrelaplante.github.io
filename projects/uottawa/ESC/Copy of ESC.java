//sudo nice --19 java -Xmx800m ESC 99980000000000 99990000000000 10000 200000000 2
//(high priority)(extra memory) (between this) (and this) (using this many S's) (using a groupSize of this) (this many threads)
//java -Xmx800m ESC 949999000000000000 950000000000000000 5000 200000000 2 40 

import java.util.HashSet;
import java.io.*;

public class ESC {
String output = "";
	static int MAXSfromfile;
	static int MAXS = 10000;
	
	public static long LOOKUPTO = 1000000000L;
	public static long STARTAT = 1;
	public static int THREADS = 2;
	public static int halfway = 40;
	long numc = 0; //the number of counterexamples
	long nums = 0; //the number of numbers that had to be proven to be squares
	long numm = 0; //the number of numbers proven to be ESC by a modular identity.

	public static long groupSize = 1000000000;

	//S[x][y] == true if S_x contains y
	boolean S[][];
	int mod[];
	int order[];

	HashSet<Long> Candidates = new HashSet<Long>();

	/* According to a paper by EUGEN J. IONASCU AND ANDREW WILSON,
	 * Only numbers congruent to these numbers, mod 9240 need
	 * to be considered.
	 * 
	 * 1^2, 13^2, 17^2, 19^2, 23^2, 29^2,
	 * 31^2, 37^2, 41^2, 43^2, 13*157, 47^2,
	 * 2521, 19*139, 2689, 53^2, 3361, 59^2,
	 * 3529, 61^2, 29*149, 67^2, 71^2, 13*397,
	 * 73^2, 5569, 31*199, 79^2, 83^2, 7561,
	 * 7681, 89^2, 8089, 8761
	 * 
	 * This is the same as:
	 * 1, 169, 289, 361, 529, 841,
	 * 961, 1369, 1681, 1849, 2041, 2209,
	 * 2521, 2641, 2689, 2809, 3361, 3481,
	 * 3529, 3721, 4321, 4489, 5041, 5161,
	 * 5329, 5569, 6169, 6241, 7561, 7681,
	 * 7921, 8089, 8761
	 * 
	 * The differences between each consecutive number
	 * will let us skip to the next number to consider
	 * during iteration through possible candidates.
	 * 
	 * differences:
	 * {169-1, 289-169, 361-289, 529-361, 841-529,
	 * 961-841, 1369-961, 1681-1369, 1849-1681, 2041-1849, 2209-2041,
	 * 2521-2209, 2641-2521, 2689-2641, 2809-2689, 3361-2809, 3481-3361,
	 * 3529-3481, 3721-3529, 4321-3721, 4489-4321, 5041-4489, 5161-5041,
	 * 5329-5161, 5569-5329, 6169-5569, 6241-6169, 7561-6241, 7681-7561,
	 * 7921-7681, 8089-7921, 8761-8089, 9241-8761}
	 * 
	 * {168,120,72,168,312,120,408,312,168,192,168,312,120,48,120,552,120,
	 *  48,192,600,168,552,120,168,240,600,72,1320,120,240,168,672,480}
	 * 
	 */
	static int skipAmount[] ={168,120,72,168,312,120,408,312,168,192,168,312,120,48,120,552,120,
		48,192,600,168,552,120,168,240,600,72,1320,120,240,168,672,480};

	public static void main(String[] args) throws InterruptedException {

		if (args.length > 0){ STARTAT = Long.parseLong(args[0]); }
		if (args.length > 1){ LOOKUPTO = Long.parseLong(args[1]); }
		if (args.length > 2){ MAXS = Integer.parseInt(args[2]); }
		if (args.length > 3){ groupSize = Integer.parseInt(args[3]); }
		if (args.length > 4){ THREADS = Integer.parseInt(args[4]); }
		if (args.length > 5){ halfway = Integer.parseInt(args[5]); }

		ESC oooo = new ESC();
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


		/*
		 * Creating the mod[] table.
		 * mod = {3,7,11,15,19,...}
		 *
		 * This is used instead of calculating 4i-1 every time
		 * we want to check if a number belongs to a particular S.
		 */
		mod = new int[MAXS];
		mod[1] = 3;
		int i=2;
		while(i < MAXS){ 
			mod[i] = mod[i-1] + 4;
			i++;
		}


		/*
		 * Creating the order[] table.
		 * order = {6,10,12,30,24,36,5,8,...}
		 *
		 * This is used to let us decide which S's to check first
		 * We check the most likely S's first, to save time.
		 */
		order = new int[MAXS];
		order[8] = 1;
		order[9] = 1;
		order[10] = 1;
		order[11] = 1;
		order[12] = 1;
		order[13] = 1;
		order[14] = 1;
		order[15] = 1;
		order[16] = 1;
		order[17] = 1;
		order[18] = 1;
		order[19] = 1;
		order[20] = 1;
		order[21] = 1;
		order[22] = 1;
		order[23] = 6; 
		order[24] = 10;
		order[25] = 12;
		order[26] = 30;
		order[27] = 24;
		order[28] = 36;
		order[29] = 18;
		order[30] = 5;
		order[31] = 8;
		order[32] = 28;
		order[33] = 60;
		order[34] = 22;
		order[35] = 42;
		order[36] = 84;
		order[37] = 40;
		order[38] = 75;
		order[39] = 20;
		order[40] = 15; 
		order[41] = 62;
		order[42] = 70;
		order[43] = 54;
		order[44] = 52;
		order[45] = 21;
		order[46] = 72;
		order[47] = 48;
		order[48] = 29;
		order[49] = 66;
		order[50] = 80;
		order[51] = 39;
		order[52] = 26;
		order[53] = 78;
		order[54] = 46;
		order[55] = 45;
		order[56] = 51;
		order[57] = 23;
		order[58] = 49;
		order[59] = 50;
		order[60] = 33;
		order[61] = 11;
		order[62] = 56;
		order[63] = 35;
		order[64] = 38;
		order[65] = 27;
		order[66] = 65;
		order[67] = 32;
		order[68] = 55;
		order[69] = 74;
		order[70] = 7;
		order[71] = 9;
		order[72] = 13;
		order[73] = 14;
		order[74] = 16;
		order[75] = 17;
		order[76] = 19;
		order[77] = 25;
		order[78] = 31;
		order[79] = 34;
		order[80] = 37;
		order[81] = 41;
		order[82] = 43;
		order[83] = 44;
		order[84] = 47;
		order[85] = 53;
		order[86] = 57;
		order[87] = 58;
		order[88] = 59;
		order[89] = 61;
		order[90] = 63;
		order[91] = 64;
		order[92] = 67;
		order[93] = 68;
		order[94] = 69;
		order[95] = 71;
		order[96] = 73;
		order[97] = 76;
		order[98] = 77;
		order[99] = 79;
		i=100;
		while(i < MAXS){
			order[i] = order[i-1] + 1;
			i++;
		}

		long startTime = System.currentTimeMillis();

		threadMessage("Starting");

		Thread t[] = new Thread[THREADS];
		int j = 0;
		while (j < THREADS) {
			t[j] = new Thread(new CounterExampleFinder(j));
			t[j].start();
			j += 1;
		}
		j = 0;
		while (j < THREADS) {
			t[j].join();
			j += 1;
		}

		threadMessage("There are "+numc+ " candidates between "+STARTAT+" and "+LOOKUPTO+ ".");
		threadMessage(nums+ " were proven to be perfect squares.");
		threadMessage(numm+ " were proven by a modular identity S<"+MAXS+".");
		threadMessage("Running time: "+ (System.currentTimeMillis() - startTime)+" ms.");
		threadMessage("The candidates are: "+ Candidates);

		//try{
		//	// Create file 
		//	FileWriter fstream = new FileWriter("data.csv");
		//	BufferedWriter out = new BufferedWriter(fstream);
		//	out.write(output);
		//	//Close the output stream
		//	out.close();
		//	}catch (Exception e){//Catch exception if any
		//	System.err.println("Error: " + e.getMessage());
		//}

	}


	private class CounterExampleFinder implements Runnable {

		int threadNumber;

		CounterExampleFinder(int tN){
			threadNumber = tN;
		}


		public void run() {

			long numGroups = (LOOKUPTO - STARTAT)/groupSize;
			long fromGroup = (threadNumber)*(numGroups)/THREADS;
			long toGroup = (threadNumber + 1)*(numGroups)/THREADS;

			for (long g = fromGroup; g < toGroup; g++){

				int skip[] = {0};
				long start = modMoreThan( STARTAT + g*(LOOKUPTO - STARTAT)/numGroups, skip);
				long end = STARTAT + (g+1)*(LOOKUPTO - STARTAT)/numGroups;


				int baseMod[] = new int[MAXS];
				int j=1;
				while(j < MAXS){ //generate the modOffset
					baseMod[j] = (int)(start % mod[j]);
					j++;
				}
				searchGroup(end - start, baseMod, start, skip);
			}

		}

	}

	public void searchGroup(long end, int baseMod[], long groupStart, int[] skip){

		long realEnd = end + groupStart;
		//threadMessage("I will be searching between "+groupStart+" and "+realEnd+".");

		int offset = 0; //n=0
		int[] threadCounting = {0,0};
		int skipIndex = skip[0];

		/* FOR EVERY n */
		while (offset < end) {

			isCounterExample(offset, groupStart, baseMod, threadCounting);

			//Skipping to the next number to be considered,
			//only have to consider certain numbers mod 9240.
			offset += skipAmount[skipIndex];				
			skipIndex = (skipIndex+1) % 33;

		} //End While

		
			numm += threadCounting[0];
			nums += threadCounting[1];

	}
	
	public void isCounterExample(int offset, long groupStart, int[] baseMod, int[] threadCounting){
		
		/*
		 * We perform 2 tests, is n a perfect square? and is n covered by an S?
		 * We check a certain number of S's, then we ask ourselves if it's square,
		 * then we check the rest of the S's.
		 * In practice, checking for square last is almost identical to checking
		 * part-way through. Checking first is time consuming.
		 * By checking a constant amount part-way through, we can be sure that we're
		 * not slowing down the program by choosing a higher MAXS.
		 */
		
		//TEST NUMBER 2: IS IT COVERED BY A MODULAR IDENTITY?
		int j = 23;
		for (j = 23 ; j < halfway; j++){
			if (S[order[j]][((offset % mod[order[j]]) + baseMod[order[j]]) % mod[order[j]]]) {
				//threadOutput += realN+","+j+"\n";
				threadCounting[0] += 1; return; //n is ESC
			}
		}
		
		//TEST NUMBER 1: IS IT A PERFECT SQUARE?
		long realN = offset + groupStart;
		long b = (long)Math.sqrt(realN);
		if (realN == b*b){ threadCounting[1] += 1; return; } //n is ESC
		//String threadOutput = "";
		
		
		//TEST NUMBER 2: IS IT COVERED BY A MODULAR IDENTITY?
		for (j = halfway ; j < MAXS; j++){
			if (S[order[j]][((offset % mod[order[j]]) + baseMod[order[j]]) % mod[order[j]]]) {
				//threadOutput += realN+","+j+"\n";
				threadCounting[0] += 1; return; //n is ESC
			}
		}
		//output += threadOutput;
		//threadnumm++;

		threadMessage(realN + " is a candidate");
		Candidates.add(realN);
		numc += 1;

		
	}

	static int m[] = {1, 169, 289, 361, 529, 841,
	   961,  1369, 1681, 1849, 2041, 2209,
	   2521, 2641, 2689, 2809, 3361, 3481,
	   3529, 3721, 4321, 4489, 5041, 5161,
	   5329, 5569, 6169, 6241, 7561, 7681,
	   7921, 8089, 8761};

	static long modMoreThan(long A, int[] skip){
	/*take a number A, return the first number
	 * bigger or equal to A that needs to be considered.
	 * skip is passed as an array in order to pass it byref.
	 */
		int i = 0;
		while (i < 1320){ //This guarentees we find one suitable number
			for (int j = 0; j<33; j++){
				
				if (A%9240==m[j]){
					skip[0] = j;
					return A;
				}
			}
			A++;
			i++;
		}
	return 0;
	}


	static void squareNumbersBetween(long a, long b, boolean[] squares, long offset){
	/* 
	 * UNUSED. returns a list of all the perfect square numbers
	 * between a and b. Seems to me like this should be faster,
	 * but it's much much slower...
	 */

		int s = (int)Math.ceil(Math.sqrt(a));
		int end = (int)Math.floor(Math.sqrt(b));
		//threadMessage("looking for squares between"+s+"^2 and "+end+"^2.");
		while (s <= end){
			//threadMessage("squares["+ (Math.pow(s,2) - offset) + "] = true");
			//squares.add((int)(Math.pow(s,2) - offset));
			squares[(int)(Math.pow(s,2) - offset)] = true;
			s++;
		}
	}
	


	//Display a message, preceded by the name of the current thread
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

}
