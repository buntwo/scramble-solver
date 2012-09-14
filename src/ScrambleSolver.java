/* ScrambleSolver.java
 * 
 * Scramble solving class.
 * 
 * Takes as constructor a board. Boards (4x4) must be typed in following format:
 * 
 * 1. Starting from row 1, left to right. Must have exactly 16 letters.
 * 2. Letter/word multipliers are denoted after the letter, as follows:
 * 		i)		DL = /
 * 		ii)		TL = //
 * 		iii)	DW = ?
 * 		iv)		TW = ??
 * 		As you will notice, the multipliers are typed with one key, with SHIFT for
 * 		word multipliers.
 * 
 * NOTE: 1. q's are interpreted as qu's.
 * 		 2. All 2 letter words are 1 point, before word multipliers.
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrambleSolver implements Iterable<Word> {
	
	private char[] board; 		// 16 elt array
	private int[] mults;		/* multipliers
								* 0 = nothing
								* 3 = DL
								* 6 = TL
								* 1 = DW
								* 2 = TW
								*/
	private int[] letterVals = {1, 4, 4, 2, 1, 4, 3, 3, 1, 10, 5, 2, 4,
							    2, 1, 4, 10, 1, 1, 1, 2, 5, 4, 8, 3, 10};
	//							    1  2  3  4  5  6   7   8   9  10  11  12  13  14  15  16
	private int[] lengthBonus = {0, 0, 0, 0, 0, 3, 6, 10, 15, 20, 20, 20, 20, 20, 20, 20, 20};
	private ArrayList<Word> foundWords;

	
	public ScrambleSolver(String theBoard) {
		
		board = new char[16];
		mults = new int[16];
		// parse board
		Pattern boardP = Pattern.compile("(?:([a-z])([/?]{0,2}))");
		Matcher boardM = boardP.matcher(theBoard);
		try {
			for(int i = 0; i < 16; ++i) {
				boardM.find();
				board[i] = boardM.group(1).charAt(0);
				String mult = boardM.group(2);
				if(mult.length() != 0) {
					if(mult.charAt(0) == '/')
						mults[i] = mult.length() * 3;
					else if(mult.charAt(0) == '?')
						mults[i] = mult.length();
				}
			}

		} catch (RuntimeException e) {
			System.err.println("Illegal board");
			e.printStackTrace();
		}
		
		Scanner r = new Scanner(System.in);
		
		foundWords = new ArrayList<Word>();
		WordComparator wc = new WordComparator();
		while(r.hasNextLine()) {
			// get word
			String curWord = r.nextLine();
			boolean[] okay = new boolean[16];
			Arrays.fill(okay, true);
			ArrayList<Word> words = getPaths(curWord, new boolean[16], okay, new ArrayList<Integer>());
			if(words.size() != 0) {
				foundWords.add(Collections.min(words, wc));
			}
		}
		Collections.sort(foundWords, wc);
		
		r.close();
		
	}
	
	// main recursive function
	private ArrayList<Word> getPaths(String word, boolean[] visited, boolean[] canGo, ArrayList<Integer> path) {
		ArrayList<Word> words = new ArrayList<Word>();
		// word matched!
		if(word.length() == 0) {
			Integer[] pathArr = path.toArray(new Integer[0]);
			words.add(new Word(getWordStr(pathArr), getScore(pathArr), pathArr));
			return words;
		}
		
		// match next char
		for(int i = 0; i < 16; ++i) {
			if ((!visited[i]) && (board[i] == word.charAt(0)) && canGo[i]) {
				// make new visited array
				boolean[] newVisited = (boolean[]) visited.clone();
				newVisited[i] = true;
				ArrayList<Integer> newPath = (ArrayList<Integer>) path.clone();
				newPath.add(i);
				boolean[] newCanGo = new boolean[16];
				for(int j = 0; j < 16; ++j) {
					if((Math.abs(j/4 - i/4) <= 1) && (Math.abs(j%4 - i%4) <= 1))
						newCanGo[j] = true;
				}
				words.addAll(getPaths(word.substring(1), newVisited, newCanGo, newPath));
			}
		}
		
		return words;
	}
	
	private String getWordStr(Integer[] path) {
		String str = "";
		for(Integer i : path) {
			str += board[i];
			if(board[i] == 'q')
				str += "u";
		}
		return str;
	}
	
	private int getScore(Integer[] path) {
		boolean dw = false;
		boolean tw = false;
		int score = 0;
		for(Integer i : path) {
			score += letterVals[(((int) board[i]) - 97)] * (mults[i] / 3 + 1);
			switch(mults[i]) {
			case 1: dw = true;
					break;
			case 2: tw = true;
					break;
			}
		}
		// 2 letter words have score 1
		if(path.length == 2)
			score = 1;
		
		// word multipler bonus
		if(tw)
			score *= 3;
		else if(dw)
			score *= 2;
		
		// word length bonus
		score += lengthBonus[path.length];
		
		return score;
	}
	
	public Iterator<Word> iterator() {
		return foundWords.iterator();
	}
	
	// returns board as a length-16 String
	public String getBoard() {
		return new String(board);
	}
	
	public int[] getMultipliers() {
		return mults;
	}
	
	// print various statistics about the board
	// number of words, max points, avg length, avg points, etc
	public void printStats() {
		int numWords = foundWords.size();
		int totPts = 0;
		int totLen = 0;
		double avgLen, avgPts;
		for(Word w : foundWords) {
			totPts += w.getScore();
			totLen += w.getWord().length();
		}
		avgLen = (double) totLen / numWords;
		avgPts = (double) totPts / numWords;
		
		System.out.println("Number of words: " + numWords);
		System.out.println("Average length: " + avgLen + " letters");
		System.out.println("Total points: " + totPts);
		System.out.println("Average point per word: " + avgPts);
	}
	
	// print all words in a list
	public void printWords() {
		for(Word w : foundWords) {
			System.out.println(w.getWord());
		}
	}
	
	// main function
	public static void main(String[] args) {
		ScrambleSolver ss = new ScrambleSolver(args[0]);
		//ss.printWords();
		int[] mults = ss.getMultipliers();
		String board = ss.getBoard();

		boolean debug = false;
		if(debug) {
			// print board
			for(int i = 1; i <= 16; ++i) {
				System.out.print(board.substring(i-1,i)+mults[i-1]+" ");
				if(i%4==0)
					System.out.println();
			}
		}
		
		System.out.println(board);
		// print all words and point values and paths
		for(Word w : ss) {
			//System.out.print(w.getWord() + " ");
			//System.out.print(w.getScore() + " ");
			Integer[] path = w.getPath();
			System.out.print(path[0]);
			for(int i = 1; i < path.length; ++i)
				System.out.print(" " + path[i]);
			System.out.println();
		}
		
		// print word stats
		//ss.printStats();
	}
}
