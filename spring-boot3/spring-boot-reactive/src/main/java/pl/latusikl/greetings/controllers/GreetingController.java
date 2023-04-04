package pl.latusikl.greetings.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.latusikl.greetings.dto.GreetingDto;
import pl.latusikl.greetings.services.GreetingService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/greetings")
@Validated
@RequiredArgsConstructor
public class GreetingController {

	private final GreetingService greetingService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/single")
	public Mono<ResponseEntity<GreetingDto>> greeting() {
		return greetingService.generateGreeting()
							  .map(ResponseEntity::ok);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/single/{delayInMilliseconds}")
	public Mono<ResponseEntity<GreetingDto>> greetingWithDelay(
			@PathVariable @Min(value = 1, message = "Delay must be greater than 0.") final int delayInMilliseconds) {
		return greetingService.generateGreetingWithDelay(delayInMilliseconds)
							  .map(ResponseEntity::ok);
	}

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, path = "/sse")
	@ResponseStatus(HttpStatus.OK)
	public Flux<GreetingDto> greetingSse() {
		return greetingService.generateGreetingSse();
	}
}
