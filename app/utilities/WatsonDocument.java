package utilities;

import java.util.ArrayList;

public class WatsonDocument {

	
	
	String id;
	
	String bodytext;
	String author;
	
	
	public WatsonDocument() {
				this.id = "";
		this.bodytext = "";
		this.author = "";
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getBodytext() {
		return bodytext;
	}


	public void setBodytext(String bodytext) {
		this.bodytext = bodytext;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	
}
