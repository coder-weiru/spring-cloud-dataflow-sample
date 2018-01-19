package scdf.example.ingestion.sink.marklogic.dbclient;

import java.io.InputStream;

public interface DocumentService {

	Document add(String docId, InputStream content, String collectionName);
}
