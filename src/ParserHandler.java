import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserHandler extends DefaultHandler{

	private BookParser bp;
	
	private boolean author;
	private boolean title;
	private boolean content;
	
	public ParserHandler(BookParser bookParser) {
		bp = bookParser;
	}
	
	public void startElement(String uri, String lName,String qName,
            				  Attributes att) throws SAXException {
		if("p".equalsIgnoreCase(qName))
			content = true;
		
		else if("author".equalsIgnoreCase(qName))
			author  = true;
		
		else if("book-title".equalsIgnoreCase(qName))
			title   = true;
	}
	
	public void endElement(String uri, String lName, String qName) 
											  throws SAXException {
		if("p".equalsIgnoreCase(qName))
			content = false;
		
		else if("author".equalsIgnoreCase(qName))
			author  = false;
		
		else if("book-title".equalsIgnoreCase(qName))
			title   = false;
	}
	
	public void characters(char characters[], int start, int length) 
											  throws SAXException {
		String data = new String(characters, start, length);
		
		if(content) {
			bp.modifyIndex(data);
		} else if(author) {
			bp.noticePostingAsAuthor(data);
		} else if(title) {
			bp.noticePostingAsTitle(data);
		}
	}
	
}
