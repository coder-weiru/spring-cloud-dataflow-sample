package scdf.example.ingestion.sink.marklogic.dbclient;

import com.marklogic.client.pojo.annotation.Id;

public class Document {
	@Id
	/**
	 * The String identifier for this user, a primary key.
	 */
	public String id;

	private String content;

	public Document(String id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}