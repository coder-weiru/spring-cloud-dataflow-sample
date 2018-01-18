package scdf.example.ingestion.opencalais;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(Processor.class)
public class OpenCalaisExtractionProcessor {

    private static Logger logger = LoggerFactory.getLogger(OpenCalaisExtractionProcessor.class);

	@Autowired
	private HttpClientCalaisPost httpClientCalaisPost;

	@Autowired
	public OpenCalaisExtractionProcessor(HttpClientCalaisPost httpClientCalaisPost) {
		super();
	}

    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Message<Document> transform(String payload) throws ParseException {
        logger.debug("Transforming payload: " + payload.toString());

		Document document = httpClientCalaisPost.postForDocument(payload);

		logger.debug("OpenCalaisExtractionProcessor transformed result: " + document);
		return MessageBuilder.withPayload(document).build();
    }
}