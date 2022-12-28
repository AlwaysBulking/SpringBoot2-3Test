package pl.latusikl.greetings.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.latusikl.greetings.config.GreetingsConfig;
import pl.latusikl.greetings.dto.GreetingDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class GreetingService {

	private static final String GREETING_MESSAGE = "Hi! Nice to see You here!";
	private static final int SSE_DELAY = 5;
	private static final String SPACE = " ";
	private final GreetingsConfig greetingConfig;

	public Mono<GreetingDto> generateGreeting() {
		return Mono.just(GREETING_MESSAGE)
				   .map(GreetingDto::new);
	}

	public Mono<GreetingDto> generateGreetingWithDelay(final int delay) {
		return Mono.just(new GreetingDto(GREETING_MESSAGE))
				   .delayElement(Duration.ofMillis(delay));
	}

	public Flux<GreetingDto> generateGreetingSse() {
		return Flux.range(0, greetingConfig.getSseCount())
				   .delayElements(Duration.ofSeconds(SSE_DELAY))
				   .map(number -> GREETING_MESSAGE + SPACE + number)
				   .map(GreetingDto::new)
				   .onBackpressureBuffer();
	}

}
