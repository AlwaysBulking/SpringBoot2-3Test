package pl.latusikl.greetings.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "greetings")
public interface GreetingsConfig {
	Integer sseCount();
}
