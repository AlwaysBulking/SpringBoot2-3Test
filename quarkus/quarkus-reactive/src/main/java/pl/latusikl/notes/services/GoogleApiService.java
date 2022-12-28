package pl.latusikl.notes.services;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.latusikl.notes.config.GoogleApiConfig;
import pl.latusikl.notes.dto.StorageResponseDto;
import pl.latusikl.notes.services.client.GoogleWebClient;
import pl.latusikl.common.ResponseStatusException;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
public class GoogleApiService {
	private final GoogleWebClient googleWebClient;
	private final GoogleTokenProvider tokenProvider;
	private final GoogleApiConfig apiConfig;

	public GoogleApiService(@RestClient final GoogleWebClient googleWebClient, final GoogleTokenProvider tokenProvider,
							final GoogleApiConfig apiConfig) {
		this.googleWebClient = googleWebClient;
		this.tokenProvider = tokenProvider;
		this.apiConfig = apiConfig;
	}

	public Uni<StorageResponseDto> postContentToBucket(final String noteContent, final String objectId) {
		return tokenProvider.getToken()
							.flatMap(token -> googleWebClient.postText(noteContent, apiConfig.bucketName(),
																	   GoogleWebClient.UPLOAD_TYPE_QUERY_VALUE, objectId, token))
							.onFailure()
							.invoke(this::handlePostImageError);
	}

	public Uni<String> getContentFromBucket(final String objectId) {
		return tokenProvider.getToken()
							.flatMap(token -> googleWebClient.getText(apiConfig.bucketName(), objectId, GoogleWebClient.ALT_VALUE,
																	  token))
							.onFailure()
							.invoke(this::handleGetImageError);
	}

	private void handleGetImageError(final Throwable throwable) {
		if (throwable instanceof ResponseStatusException exception) {
			if (exception.getHttpStatus() < 500) {
				log.warn("4XX Error returned when fetching image.");
				throw new ResponseStatusException("Image not found.", 404);
			}
			else {
				log.warn("5XX Error returned when fetching image.");
				throw new ResponseStatusException("Unable to retrieve image.", 500);
			}
		}
		throw new RuntimeException(throwable);
	}

	private void handlePostImageError(final Throwable throwable) {
		if (throwable instanceof ResponseStatusException exception) {
			log.warn("Error occurred when trying to save generated QR code.", exception);
			throw new ResponseStatusException("Unable to save created QR code", 500);
		}
		throw new RuntimeException(throwable);
	}

}
