package pl.latusikl.notes.services;

import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import pl.latusikl.notes.config.GoogleApiConfig;

import java.net.URI;
import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class Paths {

	private static final String UPLOAD_PATH_TEMPLATE = "/upload/storage/v1/b/{bucket-name}/o";
	private static final String BUCKET_NAME_PATH_PARAM = "bucket-name";
	private static final String OBJECT_IT_PATH_PARAM = "object-id";
	private static final String UPLOAD_TYPE_PARAM = "uploadType";
	private static final String UPLOAD_TYPE_VALUE = "media";
	private static final String FILE_NAME_PARAM = "name";
	private static final String GET_PATH_TEMPLATE = "storage/v1/b/{bucket-name}/o/{object-id}";
	private static final String ALT_PARAM = "alt";
	private static final String ALT_VALUE = "media";
	private final GoogleApiConfig config;

	public URI uploadPath(final String objectId) {
		return UriBuilder.of(config.getStorageApiUrl())
						 .path(UPLOAD_PATH_TEMPLATE)
						 .queryParam(UPLOAD_TYPE_PARAM, UPLOAD_TYPE_VALUE)
						 .queryParam(FILE_NAME_PARAM, objectId)
						 .expand(Map.of(BUCKET_NAME_PATH_PARAM, config.getBucketName()));
	}

	public URI downloadPath(final String objectId) {
		return UriBuilder.of(config.getStorageApiUrl())
						 .path(GET_PATH_TEMPLATE)
						 .queryParam(ALT_PARAM, ALT_VALUE)
						 .expand(Map.of(BUCKET_NAME_PATH_PARAM, config.getBucketName(), OBJECT_IT_PATH_PARAM, objectId));

	}
}
