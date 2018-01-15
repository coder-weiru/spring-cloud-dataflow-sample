package scdf.example.ingestion.tika;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.xml.sax.SAXException;

@EnableBinding(Processor.class)
public class TikaExtractionProcessor {

    private static Logger logger = LoggerFactory.getLogger(TikaExtractionProcessor.class);

	@Autowired
	private TikaParser tikaParser;

	@Autowired
	public TikaExtractionProcessor(TikaParser tikaParser) {
		super();
	}

    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Message<Document> transform(InputStream payload) throws ParseException {
        logger.debug("Transforming payload: " + payload.toString());

		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();

		try {
			tikaParser.parse(payload, metadata, handler);
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		}

		// getting the content of the document
		String doc = "{" + "   \"doc\":\"" + handler.toString() + "\"" + "}";

		logger.info("Contents of the document :" + doc);

		Document document = new Document();

		// getting metadata of the document
		logger.info("Metadata of the document:");
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			logger.info(name + " : " + metadata.getValues(name));
			document.addMetadata(name, metadata.getValues(name));
		}

		logger.debug("TikaExtractionProcessor transformed result: " + document);
		return MessageBuilder.withPayload(document).build();
    }
}