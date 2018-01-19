package scdf.example.ingestion.sink.marklogic;

import javax.validation.constraints.AssertTrue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.expression.Expression;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("marklogic")
@Validated
public class MarkLogicSinkProperties {

	private String collection;

	/**
	 * The SpEL expression to evaluate MongoDB collection
	 */
	private Expression collectionExpression;

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollection() {
		return this.collection;
	}

	public void setCollectionExpression(Expression collectionExpression) {
		this.collectionExpression = collectionExpression;
	}

	public Expression getCollectionExpression() {
		return collectionExpression;
	}

	@AssertTrue(message = "One of 'collection' or 'collectionExpression' is required")
	private boolean isValid() {
		return StringUtils.hasText(this.collection) || this.collectionExpression != null;
	}

}
