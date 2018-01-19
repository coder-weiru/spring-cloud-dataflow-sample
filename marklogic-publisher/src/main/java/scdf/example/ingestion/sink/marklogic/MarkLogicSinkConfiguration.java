/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scdf.example.ingestion.sink.marklogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.Expression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

import scdf.example.ingestion.sink.marklogic.dbclient.DocumentService;

@EnableBinding(Sink.class)
@EnableConfigurationProperties(MarkLogicSinkProperties.class)
public class MarkLogicSinkConfiguration {

	@Autowired
	private MarkLogicSinkProperties properties;

	@Autowired
	private DocumentService documentService;

	@Bean
	@ServiceActivator(inputChannel = Sink.INPUT)
	public MessageHandler mongoDbSinkMessageHandler() {
		MarkLogicStoringMessageHandler marklogicMessageHandler = new MarkLogicStoringMessageHandler(
				this.documentService);
		Expression collectionExpression = this.properties.getCollectionExpression();
		if (collectionExpression == null) {
			collectionExpression = new LiteralExpression(this.properties.getCollection());
		}
		marklogicMessageHandler.setCollectionNameExpression(collectionExpression);
		return marklogicMessageHandler;
	}

}
