package pl.latusikl.greetings.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("greetings")
public class GreetingsConfig {

	private Integer sseCount;
}
