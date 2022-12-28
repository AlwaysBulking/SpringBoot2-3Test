package pl.latusikl.greetings.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import pl.latusikl.greetings.dto.GreetingDto;
import pl.latusikl.greetings.services.GreetingService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;

@Validated
@Controller(value = "/greetings", produces = MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class GreetingController {

	private final GreetingService greetingService;

	@Get(uri = "/single", produces = MediaType.APPLICATION_JSON)
	public Mono<HttpResponse<GreetingDto>> greeting() {
		return greetingService.generateGreeting()
							  .map(HttpResponse::ok);
	}

	@Get(uri = "/single/{delayInMilliseconds}", produces = MediaType.APPLICATION_JSON)
	public Mono<HttpResponse<GreetingDto>> greetingWithDelay(
			@PathVariable @Min(value = 1, message = "Delay must be greater than 0.") final int delayInMilliseconds) {
		return greetingService.generateGreetingWithDelay(delayInMilliseconds)
							  .map(HttpResponse::ok);
	}

	@Status(HttpStatus.OK)
	@Get(uri = "/sse", produces = MediaType.TEXT_EVENT_STREAM)
	public Flux<GreetingDto> greetingSse() {
		return greetingService.generateGreetingSse();
	}
}
