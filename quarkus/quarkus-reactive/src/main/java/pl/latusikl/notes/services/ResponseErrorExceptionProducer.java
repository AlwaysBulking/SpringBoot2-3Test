package pl.latusikl.notes.services;

import lombok.extern.slf4j.Slf4j;
import pl.latusikl.common.ResponseStatusException;

import javax.ws.rs.core.Response;

@Slf4j
public class ResponseErrorExceptionProducer {

	public static ResponseStatusException createExceptionForErrorResponse(final Response response, final String externalMessage,
																		  final String internalMessage) {
		log.warn("HTTP error with code: {} received.", response.getStatus());
		log.warn("Internal message: {}", internalMessage);
		return new ResponseStatusException(externalMessage, response.getStatus());
	}

}
