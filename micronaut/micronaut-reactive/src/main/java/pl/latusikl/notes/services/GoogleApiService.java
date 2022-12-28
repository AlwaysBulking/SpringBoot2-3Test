package pl.latusikl.notes.services;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import pl.latusikl.notes.dto.StorageResponseDto;
import reactor.core.publisher.Mono;

import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;

@Singleton
@RequiredArgsConstructor
public class GoogleApiService {

	private final Paths paths;
	private final ReactorHttpClient httpClient;
	private final GoogleTokenProvider tokenProvider;

	public Mono<StorageResponseDto> postContentToBucket(final String noteContent, final String objectId) {
		return tokenProvider.getToken()
							.flatMap(token -> httpClient.retrieve(
																POST(paths.uploadPath(objectId), noteContent).header(HttpHeaders.AUTHORIZATION, token)
																											 .header(HttpHeaders.CONTENT_TYPE,
																													 MediaType.TEXT_PLAIN),
																StorageResponseDto.class)
														.next());
	}

	public Mono<String> getContentFromBucket(final String objectId) {
		return tokenProvider.getToken()
							.flatMap(token -> httpClient.retrieve(
																GET(paths.downloadPath(objectId)).header(HttpHeaders.AUTHORIZATION, token)
																								 .accept(MediaType.TEXT_PLAIN), String.class)
														.next());
	}
}
