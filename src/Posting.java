
public class Posting {

	public int  docId;
	
	public byte author;
	
	public byte title;
	
	public byte body;

	
	public Posting(int id) {
		docId  = id;
		author = 0;
		title  = 0;
		body   = 0;
	}
	
	public Posting(int id, byte a, byte t, byte b) {
		docId  = id;
		author = a;
		title  = t;
		body   = b;
	}
	
	public void merge(Posting p) {
		author = (byte) (author | p.author);
		title  = (byte) (title  | p.title );
		body   = (byte) (body   | p.body  );
	}
	
	public boolean equals(Posting p) {
		if(docId  != p.docId) return false;
		
		return true;
	}
	
}
