package pl.latusikl.notes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pl.latusikl.notes.dto.StorageResponseDto;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GoogleApiService {

	private final Paths paths;
	private final WebClient googleWebClient;
	private final WebClientErrorHandler errorHandler;
	private final GoogleTokenProvider tokenProvider;

	public Mono<StorageResponseDto> postContentToBucket(final String noteContent, final String objectId) {
		return tokenProvider.getToken()
							.flatMap(token -> googleWebClient.post()
															 .uri(paths.uploadPath(objectId))
															 .body(BodyInserters.fromValue(noteContent))
															 .header(HttpHeaders.AUTHORIZATION, token)
															 .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
															 .retrieve()
															 .onStatus(HttpStatus::isError, this::handlePostError)
															 .bodyToMono(StorageResponseDto.class));
	}

	private Mono<Throwable> handlePostError(final ClientResponse clientResponse) {
		return errorHandler.handleErrorStatus(clientResponse, HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save created note.",
											  "Failed to upload note content to the bucket.");
	}

	public Mono<String> getContentFromBucket(final String objectId) {
		return tokenProvider.getToken()
							.flatMap(token -> googleWebClient.get()
															 .uri(paths.downloadPath(objectId))
															 .header(HttpHeaders.AUTHORIZATION, token)
															 .accept(MediaType.TEXT_PLAIN)
															 .retrieve()
															 .onStatus(HttpStatus::is4xxClientError, this::handleGet4xxError)
															 .onStatus(HttpStatus::is5xxServerError, this::handleGet5xxError)
															 .bodyToMono(String.class));
	}

	private Mono<Throwable> handleGet4xxError(final ClientResponse clientResponse) {
		return this.errorHandler.handleErrorStatus(clientResponse, HttpStatus.NOT_FOUND, "Unable to retrieve note.",
												   "4XX Error returned when fetching note content.");
	}

	private Mono<Throwable> handleGet5xxError(final ClientResponse clientResponse) {
		return this.errorHandler.handleErrorStatus(clientResponse, HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve image.",
												   "Error returned when fetching image.");
	}

}
