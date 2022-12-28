package pl.latusikl.notes.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class WebClientErrorHandler {

	public Mono<Throwable> handleErrorStatus(final ClientResponse clientResponse, final HttpStatus status,
											 final String externalMessage, final String internalMessage) {
		log.warn("HTTP error with code: {} received.", clientResponse.statusCode());
		log.warn("Internal message: {}", internalMessage);
		return Mono.error(new ResponseStatusException(status, externalMessage));
	}

}
