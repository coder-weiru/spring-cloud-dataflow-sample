package scdf.example.ingestion.sink.marklogic.dbclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.marklogic.client.pojo.PojoRepository;

/**
 * Implementation of the DocumentService interface that uses the MarkLogic Java
 * Client API to implement searches and document updates.
 */
@Component
public class MarkLogicDocumentService extends MarkLogicBaseService implements DocumentService {

	@Autowired
	private PojoRepository<Document, String> repository;

	private final Logger logger = LoggerFactory.getLogger(MarkLogicDocumentService.class);

	private static String uriFromId(String collection, String id) {
		return collection + "_" + id + ".xml";
	}

	@Override
	public Document add(String id, InputStream content, String collectionName) {

		logger.info("Adding RDF document {}", id);
		Document doc = null;
		try {
		String documentUri = MarkLogicDocumentService.uriFromId(collectionName, id);

		BufferedReader buf = new BufferedReader(new InputStreamReader(content));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		String contentAsString = sb.toString();
		System.out.println("Contents : " + contentAsString);

			doc = new Document(documentUri, contentAsString);
			repository.write(doc, collectionName);
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return doc;
	}

}
