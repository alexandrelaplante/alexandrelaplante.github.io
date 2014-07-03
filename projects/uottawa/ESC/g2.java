import java.util.HashSet;
import java.io.*;

public class g2 {

	public static int MAXS = 1000;

	//S[x][y] == true if S_x contains y
	boolean S[][] = new boolean[MAXS][4*MAXS];


	public static void main(String[] args) throws InterruptedException {

		MAXS = Integer.valueOf(args[0]);
		g2 oooo = new g2();
		oooo.start();

	}

	public void start() throws InterruptedException{

		int i = 1;

		while (i < MAXS){
			S[i] = S(i);
			threadMessage("Created S" + i + ".");
			i++;
		}



		try{
			// Create file 
			FileWriter fstream = new FileWriter("out.txt");
			BufferedWriter out = new BufferedWriter(fstream);

			out.write("MAXS="+MAXS+"\n");
			i = 1;
			while (i < MAXS){
				out.write("S("+i+")=[");
				int j = 0;
				while (j < 4*MAXS){
					if (S[i][j]){ 	out.write(" " + j); }
					else {		/*out.write(" " + "0");*/ }
					j += 1;
				}
				out.write(" ]\n");
				i += 1;
			}			

			//Close the output stream
			out.close();
			}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}


	}


	public boolean[] S(int i) {

		boolean Si[] = new boolean[4*MAXS];
		HashSet<Integer> Div = Divisors(i*i);

		//HashSet<Integer> Div = new HashSet<Integer>();
		//Div.add(1);
		//Div.add(i);
		//Div.add(i*i);

		for (int D : Div){  //forall divisors D
			Si[negativeMod((-4*D), (4*i-1))] = true;
		}

		/*
		 * If n = 0 (mod any number < n) then n is not a prime
		 * number, and so is not a counter example.
		 */
		Si[0] = true;


		/* if n is equal (mod 4i-1) to a multiple of a divisor of 4i-1 (except 1)
		 * then n is not prime, and so is not a counter example.
		 * we should only take the first 100 or so of these, or else the filter
		 * list gets massive.
		 */
		HashSet<Integer> Div2 = Divisors(4*i - 1);
		for (int D : Div2){  //forall divisors D
		if (D != 1){
			int a = 1;
			while (a*D < (4*i - 1)){
				Si[a*D] = true;
				a++;
			}
		}
		}
		return Si;

	}

	public int negativeMod(int A, int B){

		while (A < 0){
			A += B;
		}
		return A;

	}

	public HashSet<Integer> Divisors(int A) {

		HashSet<Integer> D = new HashSet<Integer>();
		int i = 1;
		while (i <= Math.sqrt(A)){
			if ((A % i) == 0) { D.add(i); D.add(A/i); }
			i += 1;
		}

		//System.out.println("The divisors of " + A + " are: " + D);
		return D;

	}

	//Display a message, preceded by the name of the current thread
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

}
