import java.util.Comparator;


public class WordComparator implements Comparator<Word> {
	
	public int compare(Word a, Word b) {
		int ptDiff = b.getScore() - a.getScore();
		if (ptDiff != 0)
			return ptDiff;
		else
			return a.getWord().compareTo(b.getWord());
	}
}
