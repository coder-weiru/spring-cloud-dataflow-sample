package scdf.example.ingestion.opencalais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig {

	@Autowired
	Environment environment;

	@Bean
	public HttpClientCalaisPost httpClientCalaisPost() {
		return new HttpClientCalaisPost();
	}
}