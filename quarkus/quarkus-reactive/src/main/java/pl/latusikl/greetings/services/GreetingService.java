package pl.latusikl.greetings.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import pl.latusikl.greetings.config.GreetingsConfig;
import pl.latusikl.greetings.dto.GreetingDto;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@RequiredArgsConstructor
@ApplicationScoped
public class GreetingService {

	private static final String GREETING_MESSAGE = "Hi! Nice to see You here!";
	private static final int SSE_DELAY = 5;
	private static final String SPACE = " ";
	private final GreetingsConfig greetingConfig;

	public Uni<GreetingDto> generateGreeting() {
		return Uni.createFrom()
				  .item(GREETING_MESSAGE)
				  .map(GreetingDto::new);
	}

	public Uni<GreetingDto> generateGreetingWithDelay(final int delay) {
		return Uni.createFrom()
				  .item(new GreetingDto(GREETING_MESSAGE))
				  .onItem()
				  .delayIt()
				  .by(Duration.ofMillis(delay));
	}

	public Multi<GreetingDto> generateGreetingSse() {
		return Multi.createFrom()
					.range(0, greetingConfig.sseCount())
					.onItem()
					.call(number -> Uni.createFrom()
									   .item(number)
									   .onItem()
									   .delayIt()
									   .by(Duration.ofSeconds(SSE_DELAY)))
					.map(number -> GREETING_MESSAGE + SPACE + number)
					.map(GreetingDto::new)
					.onOverflow()
					.buffer();
	}

}
