import java.util.ArrayList;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ScrambleViewer extends JFrame {

	/**
	 * @param args
	 */

	private static final Font font = new Font("Helvetica LT Std", Font.PLAIN, 50);
	private static final Font smallFont = new Font("Helvetica LT Std", Font.PLAIN, 12);
	private static String board;
	private static int[] mults;
	private static int[] letterVals = {1, 4, 4, 2, 1, 4, 3, 3, 1, 10, 5, 2, 4,
							    2, 1, 4, 10, 1, 1, 1, 2, 5, 4, 8, 3, 10};

	public ScrambleViewer() {
		
		BoardPanel bp = new BoardPanel();
		add(bp);
		
		setBackground(Color.WHITE);
		setSize(500, 500);
		setTitle("TEST");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ScrambleViewer sv = new ScrambleViewer();
		sv.requestFocusInWindow();
		//sv.setVisible(true);
		
		ScrambleSolver ss = new ScrambleSolver(args[0]);
		ss.printStats();
		//ss.printWords();
		mults = ss.getMultipliers();
		board = ss.getBoard().toUpperCase();

		boolean debug = true;
		if(debug) {
			// print board
			for(int i = 1; i <= 16; ++i) {
				System.out.print(board.substring(i-1,i)+mults[i-1]+" ");
				if(i%4==0)
					System.out.println();
			}
			
			// print all words and point values and paths
			for(Word w : ss) {
				System.out.print(w.getWord() + " ");
				System.out.print(w.getScore() + " ");
				for(int i : w.getPath()) {
					System.out.print(i + "-");
				}
				System.out.println();
			}
		}
		
		/*
		drawGrid(     ;
		String pathStr = "13 1 2 7 11";
		String[] path = pathStr.split(" ");
		drawPath(path);

		drawLetters();
		 */

		for(Word w : ss) {
			drawGrid();
			drawPath(w.getPath());
			drawLetters();
			drawWord(w.getWord().toUpperCase() + " " + w.getScore());

			// wait for space
			while(true) {
				if(StdDraw.hasNextKeyTyped()) {
					if(StdDraw.nextKeyTyped() == ' ')
						break;
				}
			}
		}

	}

	private static void drawGrid() {
		StdDraw.clear();
		StdDraw.setXscale(-25, 125);
		StdDraw.setYscale(-125, 25);
		StdDraw.setFont(font);
		StdDraw.setPenRadius();

		for(int i=0; i<=4; ++i) {
			StdDraw.line(0, -25*i, 100, -25*i);
			StdDraw.line(25*i, 0, 25*i, -100);
		}
		//StdDraw.setPenColor(Color.DARK_GRAY);
		//StdDraw.filledRectangle(50, -50, 50, 50);
	}
	
	private static void drawLetters() {
		StdDraw.setPenColor();
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				// letter
				String s = board.substring(4*i+j, 4*i+j+1);
				if(s.equals("Q"))
					s = "Qu";
				StdDraw.setFont(font);
				StdDraw.text(25*(j + 0.5), -25*(i + 0.5) - 1, s);
				
				// point value
				StdDraw.setFont(smallFont);
				StdDraw.text(25*(j + 0.5)+10, -25*(i + 0.5) +10, ""
									+ letterVals[((int) s.charAt(0)) - 65]);
				
				// multiplier
				if(mults[4*i+j] != 0) {
					String str = "";
					switch(mults[4*i+j]) {
					case 1: str = "DW"; break;
					case 2: str = "TW"; break;
					case 3: str = "DL"; break;
					case 6: str = "TL"; break;
					}
					StdDraw.text(25*(j + 0.5)-9, -25*(i + 0.5)+10, str);
				}
			}
		}
	}
	
	private static void drawWord(String w) {
		// print the word
		StdDraw.setPenColor();
		StdDraw.setFont(font);
		StdDraw.text(50, -115, w);
	}

	private static void drawPath(Integer[] path) {
		if(path.length == 0)
			return;
		
		int last = path[0];
		// color last square red
		StdDraw.setPenColor(Color.GREEN);
		StdDraw.filledRectangle((last % 4 + 0.5) * 25, -((int) (last/4) + 0.5)*25, 12.5, 12.5);
		
		int first = last;
		StdDraw.setPenColor(Color.DARK_GRAY);
		StdDraw.setPenRadius(0.010);
		
		for(int i = 1; i < path.length; ++i) {
			int tmp = first;
			first = path[i];
			last = tmp;
			// fill in square
			//StdDraw.setPenColor(new Color((int) (255*(path.length - i + 1)/path.length),
								  //(int) (255*(i-1)/path.length), 0));
			//StdDraw.setPenIColor(Color.GRAY);
			//StdDraw.setPenColor(new Color(0, 255, 0, (int) (200*(i + 1)/path.length) + 55));
			//StdDraw.filledRectangle((first % 4 + 0.5) * 25, -((int) (first/4) + 0.5)*25, 12.5, 12.5);
			
			// draw line
			// white line
			//StdDraw.setPenColor(Color.WHITE);
			//StdDraw.setPenRadius(0.030);
			//StdDraw.line((last % 4 + 0.5) * 25, -((int) (last/4) + 0.5)*25,
			//			  (first % 4 + 0.5) * 25, -((int) (first/4) + 0.5)*25);
			// black line
			StdDraw.setPenColor();
			StdDraw.setPenRadius(0.010);
			StdDraw.line((last % 4 + 0.5) * 25, -((int) (last/4) + 0.5)*25,
					(first % 4 + 0.5) * 25, -((int) (first/4) + 0.5)*25);

		}

		StdDraw.setPenColor(Color.RED);
		StdDraw.filledRectangle((first % 4 + 0.5) * 25, -((int) (first/4) + 0.5)*25, 12.5, 12.5);
	}
	
	
	//------------------------------------------PRIVATE CLASS--------------------------------
	
	private class BoardPanel extends JPanel {
		
		public BoardPanel() {
			
		}
		
		public void paintComponent(Graphics g) {
			g.drawLine(0, 0, 100, 30);
			
		}
	}
	
}
