
public class Word {
	
	private String word;
	private int score;
	private Integer[] path;
	
	public Word(String word, int score, Integer[] path) {
		this.word = word;
		this.score = score;
		this.path = path;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getScore() {
		return score;
	}
	
	public Integer[] getPath() {
		return path;
	}
}
