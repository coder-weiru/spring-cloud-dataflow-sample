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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import scdf.example.ingestion.sink.marklogic.dbclient.DocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
@TestPropertySource(properties = "marklogic.collection=testing")
public class MarkLogicSinkApplicationIT {
	@Autowired
	protected DocumentService documentService;

	@Autowired
	protected Sink sink;

	@Autowired
	protected MarkLogicSinkProperties markLogicSinkProperties;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testWiring() throws IOException {

		Map<String, Object> headers = new HashMap<>();

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("test-documents/testXml.xml");

		BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		String fileAsString = sb.toString();
		System.out.println("Contents : " + fileAsString);

		headers.put("content-type", "text/plain");
		Message<String> message = new GenericMessage<>(fileAsString, headers);
		this.sink.input().send(message);
	}

}
