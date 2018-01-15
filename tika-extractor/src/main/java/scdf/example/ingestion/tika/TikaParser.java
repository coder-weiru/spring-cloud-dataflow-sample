package scdf.example.ingestion.tika;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TikaParser {

	private final Parser parser;

	public TikaParser(final String tikaConfig) throws TikaException {
		if (tikaConfig == null || tikaConfig.length() == 0) {
			parser = new AutoDetectParser();
		} else {
			final InputStream is = new ByteArrayInputStream(tikaConfig.getBytes());
			try {
				final TikaConfig conf = new TikaConfig(is);
				parser = new AutoDetectParser(conf);
			} catch (TikaException | IOException | SAXException e) {
				throw new TikaException(e.getMessage(), e);
			}
		}
	}

	/*
	 * Map<MediaType, Parser> parsers = ((AutoDetectParser)
	 * parser).getParsers(); parsers.put(MediaType.APPLICATION_XML, new
	 * HtmlParser()); ((AutoDetectParser) parser).setParsers(parsers);
	 */
	public static ContentHandler newWriteOutBodyContentHandler(Writer w, int writeLimit) {
		final ContentHandler writeOutContentHandler = new WriteOutContentHandler(w, writeLimit);
		return new BodyContentHandler(writeOutContentHandler);
	}

	public void parse(InputStream stream, Metadata metadata, ContentHandler handler)
			throws IOException, SAXException, TikaException {
		ParseContext context = new ParseContext();
		context.set(Parser.class, parser);
		parser.parse(stream, handler, metadata, context);
	}

}
