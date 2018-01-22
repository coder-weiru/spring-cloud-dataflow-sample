package scdf.example.ingestion.sink.marklogic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.springframework.expression.Expression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.Assert;

import scdf.example.ingestion.sink.marklogic.dbclient.DocumentService;
import scdf.example.ingestion.sink.marklogic.dbclient.MarkLogicDocumentService;

/**
 * Implementation of {@link MessageHandler} which writes Message payload into a
 * MarkLogic collection identified by evaluation of the
 * {@link #collectionNameExpression}.
 *
 * @author Wei Ru
 *
 */
public class MarkLogicStoringMessageHandler extends AbstractMessageHandler {

	private volatile DocumentService documentService;

	private volatile StandardEvaluationContext evaluationContext;

	private volatile Expression collectionNameExpression = new LiteralExpression("data");

	private volatile boolean initialized = false;

	/**
	 * Will construct this instance using provided {@link DocumentService}
	 *
	 * @param documentService
	 *            The document service.
	 */
	public MarkLogicStoringMessageHandler(DocumentService documentService) {
		Assert.notNull(documentService, "'documentService' must not be null");

		this.documentService = documentService;
	}

	/**
	 * Sets the SpEL {@link Expression} that should resolve to a collection name
	 * used by {@link MongoOperations} to store data
	 *
	 * @param collectionNameExpression
	 *            The collection name expression.
	 */
	public void setCollectionNameExpression(Expression collectionNameExpression) {
		Assert.notNull(collectionNameExpression, "'collectionNameExpression' must not be null");
		this.collectionNameExpression = collectionNameExpression;
	}

	@Override
	public String getComponentType() {
		return "mongo:outbound-channel-adapter";
	}

	@Override
	protected void onInit() throws Exception {
		this.evaluationContext = ExpressionUtils.createStandardEvaluationContext(this.getBeanFactory());
		if (this.documentService == null) {
			this.documentService = new MarkLogicDocumentService();
		}
		this.initialized = true;
	}

	@Override
	protected void handleMessageInternal(Message<?> message) throws Exception {
		Assert.isTrue(this.initialized, "This class is not yet initialized. Invoke its afterPropertiesSet() method");
		String collectionName = this.collectionNameExpression.getValue(this.evaluationContext, message, String.class);
		Assert.notNull(collectionName, "'collectionNameExpression' must not evaluate to null");

		Object payload = message.getPayload();
		String payloadAsString = (String)payload;
		InputStream stream = new ByteArrayInputStream(payloadAsString.getBytes(StandardCharsets.UTF_8.name()));
		String id = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis())) + "_"
				+ UUID.randomUUID().toString();
		this.documentService.add(id, stream, "SCDF_DOC");
	}
}
