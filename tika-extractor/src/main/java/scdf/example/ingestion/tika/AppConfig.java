package scdf.example.ingestion.tika;

import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	Environment environment;

	@Bean
	public TikaParser tikaParser() throws TikaException {
		return new TikaParser(null);
	}
	

}