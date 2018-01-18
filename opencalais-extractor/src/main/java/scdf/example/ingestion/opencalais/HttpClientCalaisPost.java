package scdf.example.ingestion.opencalais;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;


public class HttpClientCalaisPost {

	private static final String CALAIS_URL = "https://api.thomsonreuters.com/permid/calais";
	public static String uniqueAccessKey = "g2erFUFoltOXxhrrGZdznx6hyH5LzVFh";

	private HttpClient client;

	public HttpClientCalaisPost() {
		super();
		client = new HttpClient();
	}

	private PostMethod createPostMethod() {

		PostMethod method = new PostMethod(CALAIS_URL);

		// Set mandatory parameters
		method.setRequestHeader("X-AG-Access-Token", uniqueAccessKey);

		method.setRequestHeader("Content-Type", "text/raw");

		// Set response/output format
		method.setRequestHeader("outputformat", "xml/rdf" /* "application/json" */);

		client.getParams().setParameter("http.useragent", "Calais Rest Client");

		return method;
	}

	public Document postForDocument(String input) {
		PostMethod method = null;
		Document document = null;
		try {
			method = createPostMethod();
			method.setRequestEntity(new StringRequestEntity(input, "text/raw", "UTF-8"));
			int returnCode = client.executeMethod(method);
			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err.println("The Post method is not implemented by this URI");
				// still consume the response body
				method.getResponseBodyAsString();
			} else if (returnCode == HttpStatus.SC_OK) {
				System.out.println("InputStream post succeeded. " + input);
				document = new Document();

				StringWriter writer = null;
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
					writer = new StringWriter();
					String line;
					while ((line = reader.readLine()) != null) {
						writer.write(line);
					}

					document.setContent(writer.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (writer != null)
						try {
							writer.close();
						} catch (Exception ignored) {
						}
				}

			} else {
				System.err.println("InputStream post failed. " + input);
				System.err.println("Got code: " + returnCode);
				System.err.println("response: " + method.getResponseBodyAsString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}

		return document;
	}

}
