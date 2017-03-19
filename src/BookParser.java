import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class BookParser {
	
	public File   book;
	private Index index;
	private int   ident;
	
	public BookParser(File file, int docId, Index modIndex) {
		book  = file;
		index = modIndex;
		ident = docId;
	}
	
	public void parseBook() {
		try {
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			
			ParserHandler handler = new ParserHandler(this);
			parser.parse(book, handler);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void modifyIndex(String data) {
		String tokens[] = pullTokens(data);
		
		for(String token: tokens) {
			pushToken(token, (byte)0, (byte)0, (byte)1);
		}
	}
	
	public void noticePostingAsAuthor(String data) {
		String tokens[] = pullTokens(data);
		
		for(String token: tokens) {
			pushToken(token, (byte)1, (byte)0, (byte)0);
		}
	}
	
	public void noticePostingAsTitle(String data) {
		String tokens[] = pullTokens(data);
		
		for(String token: tokens) {
			pushToken(token, (byte)0, (byte)1, (byte)0);
		}
	}
	
	private void pushToken(String token, byte author, byte title, byte body) {
		if(!index.containsToken(token)) 
			index.addToken	 (token, new Posting(ident, author, title, body));
		
		else						  	
			index.updateToken(token, new Posting(ident, author, title, body));
	}
	
	private String[] pullTokens(String data) {
		String[] tokens = data.split(" ");
		
		for(int i = 0; i < tokens.length; i++) {
			tokens[i] = ZoneIndex.distinct(tokens[i].toLowerCase());
		}
		
		return tokens;
	}

}
