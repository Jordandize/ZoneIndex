import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ZoneIndex implements Index{

	public static final int INDEX_START_SIZE = 21844;
	public static final String OUTPUT_FILE 	 = "ZoneIndex.txt";
	
	private File[] collection;
	private Map<String, ArrayList<Posting>> index;
	
	public ZoneIndex(File[] body) {
		collection = body.clone();
		index = new HashMap<>();
		constraction();
	}
	
	private void constraction() {
		initIndex();
		saveIndex();
	}
	
	private void initIndex() {
		BookParser parser = null;
		
		for(int i = 0; i < collection.length; i++) {
			parser = new BookParser(collection[i], i, this);
			parser.parseBook();
		}
		
		index = new TreeMap<>(index);
	}
	
	private void saveIndex() {
		try {
			FileWriter     stream = new FileWriter(OUTPUT_FILE);
			BufferedWriter writer = new BufferedWriter(stream);
			
			StringBuilder build = new StringBuilder();
			for(String token: index.keySet()) {
				build.setLength(0);
				build.append(token);
				
				for(Posting posting: index.get(token)) {
					build.append(" ");
					build.append(posting.docId)
						 .append(posting.author)
						 .append(posting.title)
						 .append(posting.body);
				}
				
				build.append('\n');
				writer.write(build.toString());
			}
			
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addToken(String token, Posting posting) {
		ArrayList<Posting> list = new ArrayList<>();
		list.add(posting);
		index.put(token, list);
	}
	
	public void updateToken(String token, Posting posting) {
		ArrayList<Posting> list = index.get(token);
		Posting lastpost = list.get(list.size()-1);
		
		if(!lastpost.equals(posting)) {
			list.add(posting);
			index.put(token, list);
		} else {
			lastpost.merge(posting);
		}
	}
	
	public boolean containsToken(String token) {
		return index.containsKey(token);
	}
	
	public ArrayList<Posting> getPostingList(String token) {
		return index.get(token);
	}
	
	public static String distinct(String token) {
		StringBuilder buil = new StringBuilder();
		
		for(int j = 0; j < token.length(); j++) {
			char ch = token.charAt(j);
			if(Character.isLetterOrDigit(ch))
				buil.append(ch);
		}
		
		return buil.toString();
	}

	public static void main(String[] args) {
		File[] collection = new File("fb2").listFiles();
		
		@SuppressWarnings("unused")
		ZoneIndex zi = new ZoneIndex(collection);
		
		System.out.println("OK");
	}

}
