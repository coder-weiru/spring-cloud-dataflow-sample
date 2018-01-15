package scdf.example.ingestion.tika;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Document {

	private Map<String, Object> metadata;

	public Document() {
		metadata = new HashMap<String, Object>();
	}

	public void addMetadata(String name, String[] value) {
		metadata.put(name, value);
	}

	public Object getMetadata(String name) {
		return metadata.get(name);
	}

	public Set<String> metadataNames() {
		return metadata.keySet();
	}

	public void removeMetadata(String name) {
		metadata.remove(name);
	}

	public void setMetadata(String name, Object value) {
		metadata.put(name, value);
	}

	public int metadataSize() {
		return metadata.size();
	}

	public String toString() {
		return metadata.toString();
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

}
