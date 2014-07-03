import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.io.*;

/* If the remaining letters have no vowels, cut da branch */
public class calc {

	static int maxLetters = 98;
	static int maxRepeatedLetter = 0;
	static int alphSize = 26;
	//static char[] alphabet = new char[alphSize];
	static char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	wordSet S;
	String initialLetters;
	long startTime;
	
	public static void main(String[] args) {
		

		calc x = new calc();
		x.run();
		
	}
	
	private void run() {
		
		/*
		 * S = a set of possible words
		 * a = answer
		 * 
		 * naive algorithm: try all possible words and check if it uses the right letters
		 * backtrack algorithm: pick a random word that's still possible
		 * 
		 * backtrack when: no vowels in letters left
		 * backtrack when: no words in words left
		 * backtrack when: letters left > words left
		 * 
		 * dynamic program some memory so that most requested letters are remembered
		 */
		
		
		initialLetters = "uttensiosicvis"; //HOOKE'S LAW
		//initialLetters = "dataaequationequotcunquefluentesquantitatesinvolventefluxionesinvenireetviceversa"; //NEWTON CALCULUS
		//initialLetters = "ttttttttttttooooooooooeeeeeeeeaaaaaaallllllnnnnnnuuuuuuiiiiisssssdddddhhhhhyyyyyrrrfffbbwwkcmvg";
		//without III encoding fundamental
		//initialLetters = "tttttttttttoooooooooeeeeeeaaaaalllllnnuuuuuiiiisssssdddhhhhhyyyyyrrrffbbwwkv";
		int letters[] = new int[alphSize];
		//create the initial letters[]
		for(int i = 0; i < initialLetters.length(); i++){
			letters[getCode(initialLetters.charAt(i))] += 1;
		}
		//determine maxRepeatedLetter
		for(int i=0; i<alphSize;i++) {
			if (letters[i] > maxRepeatedLetter){ maxRepeatedLetter = letters[i]; }
		}
		
		HashSet<String> words = new HashSet<String>();
		
		
		words.add("ut"); words.add("tensio"); words.add("vis"); words.add("sic");
		words.add("vir"); words.add("sub"); words.add("ter"); words.add("tu");
		words.add("tui"); words.add("tutis"); words.add("totus"); words.add("tot");
		words.add("ter"); words.add("vae"); words.add("vel"); words.add("vere");
		words.add("via"); words.add("vir"); words.add("vito"); words.add("vita");
		
		words.add("et"); words.add("vice"); words.add("versa"); words.add("ac");
		words.add("is"); words.add("ovis"); words.add("te"); words.add("tunc");
		words.add("iste"); words.add("st"); words.add("vinco"); words.add("se");
		words.add("insto"); words.add("intus"); words.add("sui"); words.add("sto");
		words.add("toties"); words.add("viscus"); words.add("incito"); words.add("sive");
		words.add("us"); words.add("in"); words.add("sicut"); words.add("vestis");
		words.add("os"); words.add("si"); words.add("victus"); words.add("vesco");
		words.add("tui"); words.add("nisi"); words.add("vos"); words.add("no");
		
		
		int j = 0;
		try{
		    FileInputStream fstream = new FileInputStream("latin.txt");
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	String line[] = strLine.split("[ ,:.]");
		      //words[j] = strLine;
		    	words.add(line[0].toLowerCase());
		    	//System.out.println(line[0].toLowerCase());
		      j++;
		      //if(j>500){ break; }
		    }
		    //Close the input stream
		    in.close();
		}catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		}
		
		wordSet dict= new wordSet(words);
		
		startTime = System.currentTimeMillis();
		
		//backtrack("","",dict, letters);
		outputILP(words, letters);
	}
	
	public void outputILP(HashSet<String> words, int[] letters){
		System.out.print("max: ");
		System.out.print(alphabet[0]);
		for(int i=1; i<alphSize; i++){
			System.out.print("+"+alphabet[i]);
		}
		System.out.println(";");
		
		System.out.println("");
		System.out.println("");
		System.out.println("/* Letter constraints */");
		
		for(int i=0; i<alphSize; i++){
			System.out.println(alphabet[i]+" <= "+letters[i]+";");
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("/* Word constraints */");
		
		int wNum = 1;
		for(String w : words){
			int[] wletters = new int[alphSize];
			for(int i=0; i<w.length(); i++){
				wletters[getCode(w.charAt(i))] += 1;
			}
			for(int i=0; i<alphSize; i++){
				System.out.println("w"+wNum+alphabet[i]+" = "+wletters[i]+" w"+wNum+";");
			}
			wNum++;
			System.out.println();
		}
		
		for(int i=0; i<alphSize; i++){
			System.out.print(alphabet[i] +" = w1"+alphabet[i]);
			for(int n=2; n<wNum; n++){
				System.out.print(" + w"+n+alphabet[i]);
			}
			System.out.println(";");
		}
		
		System.out.println("");
		System.out.println(" /* integer constraints */");
		System.out.print("int w1");
		for(int n=2; n<wNum; n++){
			System.out.print(", w"+n);
		}
		System.out.print(";");
		
	}
	
	
	public void backtrack(String partial, String lastWord, wordSet dict, int[] letters){
		
        String testline = partial.replaceAll("[^a-zI]+", "");
        int diff = testline.length() - initialLetters.length();
		if(diff >= 0){
			System.out.println(diff+" SOL: "+partial);
			if (diff==0){
				System.out.println(System.currentTimeMillis() - startTime);
				//System.exit(0);
			}
		}
		
		HashSet<String> nextPartials = dict.get(letters);

		//remove words that aren't in lexicographic order
		Iterator<String> it = nextPartials.iterator();
		while(it.hasNext()){ //ensuring lexicographic order
			String m = (String) it.next();
			if(m.compareTo(lastWord) < 0){
				it.remove();
			}
		}
		
		
		if (nextPartials.size() == 0){
			//System.out.println("BACKTRACK");
			return; //we're at the end of this branch, BACKTRACK
		} else {
			
			int sumOfVowels = letters[0]+letters[4]+letters[8]+letters[14]+letters[20]+letters[24];
			if (sumOfVowels == 0){ 
				//System.out.println("BACKTRACK");
				return;//no vowels left, BACKTRACK
			}
						
			int sumOfCons=0;
			for(int i=0; i<alphSize; i++){
				if(i!=0 && i!=4 && i!=8 && i!=14 && i!=20 && i!=24){
					sumOfCons += letters[i];
				}
			}
			
			if(sumOfVowels < (1/3)*sumOfCons){
				//System.out.println("BACKTRACK");
				return;//not enough vowels left, BACKTRACK
			}
			
			
			ArrayList<String> nexts = new ArrayList<String>(nextPartials);
			Collections.sort(nexts);
			
			//if I have letters that don't appear in any possible word, backtrack
			String concat = "";
			boolean appears[] = new boolean[alphSize];
			for(String m : nexts){
				concat += m;
			}
			for(int i=0; i<concat.length(); i++){
				appears[getCode(concat.charAt(i))] = true; //this letter does appear
			}
			for(int i=0; i<alphSize; i++){
				if (!appears[i] && letters[i] > 0){
					//System.out.println("BACKTRACK");
					return; //we have a letter that can never be used, BACKTRACK
				}
			}
			
			if(concat.length() < Math.abs(diff)){
				//System.out.println("BACKTRACK");
				return; //assuming NO REPEATS past this point, not enough words to complete this partial, BACKTRACK
			}
			
			//System.out.println(partial +" "+ nexts+" "+diff);
			
			for(String n : nexts){
				int[] newLetters = letters.clone();
				for(int i = 0; i < n.length(); i++){
					newLetters[getCode(n.charAt(i))] -= 1;
				}
				backtrack(partial +" "+ n, n, dict, newLetters);
			}			
		}
	}
	
	

	

	
	
	public class wordSet {
		
		/*
		 * idea:
		 * HashSet<String> [][] S
		 * 
		 * S[0][2] is the HashSet of all words with at most 2 a's
		 * 
		 * get(letters[]):
		 * return S[0][letters[0]] intersect S[1][letters[1]] intersect etc...
		 * 
		 *  s[0][letters[0]]
		 * 
		 * 
		 */
		
		
		// /*



		HashSet<String> S[][] = new HashSet[alphSize][maxRepeatedLetter+1];
		HashSet<String> S2[] = new HashSet[alphSize]; //S2[0] words with at least 1 a.
		
		//ArrayList<ArrayList<HashSet<String>>> S = new ArrayList<ArrayList<HashSet<String>>>();
		
		
		
		public wordSet(HashSet<String> words){
			
			for (int i=0; i<alphSize;i++){
				for (int j=0; j<=maxRepeatedLetter; j++){
					HashSet<String> Sij = new HashSet<String>();
					for(String w : words){
						
						int countI = 0; //counts how many times this letter appears in w
						for (int k=0; k<w.length();k++){
							if(getCode(w.charAt(k)) == i){ countI++; }
						}
						if(countI <= j){ //if this word has at most j times the letter i, it belongs to S[i][j]  
							Sij.add(w);
						}
					}
					S[i][j] = Sij;
				}	
			}
			
			for(int i=0;i<alphSize;i++){
				HashSet<String> S2i = new HashSet<String>();
				for(String w : words){
					int countI = 0; //counts how many times this letter appears in w
					for (int k=0; k<w.length();k++){
						if(getCode(w.charAt(k)) == i){ countI++; }
					}
					if(countI >= 1){ //at least once  
						S2i.add(w);
					}
				}
				S2[i] = S2i;
			}
			
		}
		
		

		public HashSet<String> get(int[] letters){
			
			HashSet<String> answer = new HashSet<String>();
			
			int minLetter = 0;
			int min = 999;
			for(int i=0; i<alphSize;i++){
				if(S[i][letters[i]].size() < min){
					min = S[i][letters[i]].size();
					minLetter = i; 
				}
			}
			
			//answer is all words with at most letters[0] a's, except those that don't satisfy all other conditions
			answer.addAll(S[minLetter][letters[minLetter]]);
			
			
			//remove words that don't appear in EVERY other set (this is taking the intersection)
			for(int i=0;i<alphSize;i++){
				if (i!= minLetter){
					
					Iterator<String> it = answer.iterator();
					while(it.hasNext()){
						String m = (String) it.next();
						int li = letters[i];
						HashSet<String>[] Si = S[i];
						if (!Si[li].contains(m)){
						//if (!S[i][letters[i]].contains(m)){
							it.remove();
						}
					}
				}
			}
			
			
			return answer;
			
		}
		
		
		public HashSet<String> get2(int[] need) {
			/*
			 * Returns the set of words that use these letters
			 */
			HashSet<String> answer = new HashSet<String>();
			
			for(int i=0; i<alphSize; i++){
				if (need[i]>0){
					answer.addAll(S2[i]);
				}
			}
			
			
			
			
			return answer;
			
		}
		
		
		//  */

		/*
		tuple[][] S = new tuple[alphSize][];
		int[][] ii = new int[alphSize][maxLetters];//Ex: ii[0][3] is the last index of the tuple with freq 3 in Sa
		int length;
		
		public wordSet(String[] words){
			length = words.length;
			for (int alph = 0; alph < alphSize; alph++){
				S[alph] = new tuple[length];
				for (int i = 0; i < length; i++){
					S[alph][i] = new tuple(words[i], alphabet[alph]);
				}
				Arrays.sort(S[alph]);
				for (int i = 0; i < maxLetters; i++){
					ii[alph][i] = length;
				}
				for (int i = 0; i < length; i++){
					ii[alph][S[alph][i].getFreq()] = i;
				}
			}
			
			//System.out.println("S_t sorted:");
			//for (int i = 0; i < length; i++){
			//	System.out.println(S[0][i]);
			//}

		}
		
		public HashSet<String> get(int[] letters, String lastWord){
			
			HashSet<String> answer = new HashSet<String>(length);
			int i;
			
			//Instead of a, this should be the smallest of ia[a],ib[b],ic[c],id[d]
			int min = 999;
			int minLetter = 0;
			for (int j = 0; j < alphSize; j++){
				//System.out.println("letters[j] = " + letters[j] + " with j = " + j + " with alphabet[j] = " + alphabet[j]);
				if (ii[j][letters[j]] < min){ //if (ia[#a])
					min = ii[j][letters[j]];
					minLetter = j;
				}
			}
			//System.out.println("Turns out minLetter is " + minLetter);
			i = ii[minLetter][letters[minLetter]];
			if (i == length) { i = length -1; }
			while( i >= 0 ){
				//if(S[minLetter][i].getWord().compareTo(lastWord) > 0){ //ensuring lexicographic order
					answer.add(S[minLetter][i].getWord());
					//System.out.println("a just added " + S[0][i].getWord());
					i--;
				//}
			}
			//Now get rid of all the ones that are not in EVERY other set
			for (int alph = 0; alph < alphSize; alph++){
				if (alph != minLetter){ //so that it's ever OTHER set
					//System.out.println("letters[alph] = " + letters[alph] + " alph = "+alph);
					i = ii[alph][letters[alph]] + 1;
					while (i < length){
						answer.remove(S[alph][i].getWord());
						//System.out.println("Since we have " + letters[alph] + " " + alphabet[alph] + " just removed " + S[alph][i].getWord());
						i++;
					}
				}
			}
			
			Iterator<String> it = answer.iterator();
			while(it.hasNext()){ //ensuring lexicographic order
				String m = (String) it.next();
				if(m.compareTo(lastWord) < 0){
					it.remove();
				}
			}
			//System.out.println(answer);
			return answer;
		}
		
		*/
		
	}
	
	
	public class tuple implements Comparable<tuple>{
		String word;
		int n;
		
		public tuple(String w, char letter){
			
			word = w;
			/* counting the number 'letter' in the word */
			for (int i=0; i< w.length(); i++){
				if (w.charAt(i) == letter){
					n++;
				}
			}
		}
		
		public String getWord(){
			return word;
		}
		public int getFreq(){
			return n;
		}
		
		public String toString(){
			return word + n;
		}

		public int compareTo(tuple o) {
			return n - o.getFreq();
		}


	}
	
	private int getCode(char letter) {
		switch (letter){
		case 'a': return 0;
		case 'b': return 1;
		case 'c': return 2;
		case 'd': return 3;
		case 'e': return 4;
		case 'f': return 5;
		case 'g': return 6;
		case 'h': return 7;
		case 'i': return 8;
		case 'j': return 9;
		case 'k': return 10;
		case 'l': return 11;
		case 'm': return 12;
		case 'n': return 13;
		case 'o': return 14;
		case 'p': return 15;
		case 'q': return 16;
		case 'r': return 17;
		case 's': return 18;
		case 't': return 19;
		case 'u': return 20;
		case 'v': return 21;
		case 'w': return 22;
		case 'x': return 23;
		case 'y': return 24;
		case 'z': return 25;
		}
		return 1000;
	}

}
