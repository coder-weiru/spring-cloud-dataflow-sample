package scdf.example.ingestion.sink.marklogic.dbclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.pojo.PojoRepository;

import scdf.example.ingestion.sink.marklogic.security.ClientRole;


@Component
@ComponentScan
@PropertySource("classpath:application.properties")
public class DatabaseContext {

	@Autowired
	/** Spring provides this object at startup, for access to environment configuration
	 */
	private Environment env;
	
	@Bean
	/**
	 * Makes a HashMap of Client objects available to the application.
	 * @return A Clients class, which extends HashMap<ClientRole, DatabaseClient>;
	 */
	public Clients clients() {
		Clients clients = new Clients(env);
		return clients;
	}
	
	@Bean
	/**
	 * This repository object manages operations for the Document POJO Class.
	 * Generally accessed through calls to the DocumentService, which mediates
	 * and limits some of the access.
	 * 
	 * @return A PojoRepository object to manage Documents.
	 */
	public PojoRepository<Document, String> repository() {
		return clients().get(ClientRole.DOCUMENT_WRITER)
				.newPojoRepository(Document.class, String.class);
	}
	
	@Bean
	/**
	 * Initializes a singleton ObjectMapper.
	 * @return A Jackson ObjectMapper implementation for the Spring IoC container
	 */
	public ObjectMapper mapper() {
		return new CustomObjectMapper();
	}
	
}
