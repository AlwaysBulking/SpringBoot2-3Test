package pl.latusikl.notes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GoogleWebClientConfig {

	@Bean
	WebClient googleWebClientDelegate() {
		return WebClient.create();
	}
}
