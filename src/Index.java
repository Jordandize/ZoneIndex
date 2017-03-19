import java.util.ArrayList;

public interface Index {
	
	void addToken(String token, Posting posting);
	
	void updateToken(String token, Posting posting);
	
	boolean containsToken(String token);
	
	ArrayList<Posting> getPostingList(String token);
	
}
