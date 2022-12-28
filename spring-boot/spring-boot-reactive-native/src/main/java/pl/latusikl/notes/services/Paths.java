package pl.latusikl.notes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.latusikl.notes.config.GoogleApiConfig;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class Paths {

	private static final String UPLOAD_PATH_TEMPLATE = "/upload/storage/v1/b/{bucket-name}/o";
	private static final String UPLOAD_TYPE_PARAM = "uploadType";
	private static final String UPLOAD_TYPE_VALUE = "media";
	private static final String FILE_NAME_PARAM = "name";
	private static final String GET_PATH_TEMPLATE = "storage/v1/b/{bucket-name}/o/{object-id}";
	private static final String ALT_PARAM = "alt";
	private static final String ALT_VALUE = "media";
	private final GoogleApiConfig config;

	public URI uploadPath(final String objectId) {
		return UriComponentsBuilder.fromUriString(config.getStorageApiUrl())
								   .path(UPLOAD_PATH_TEMPLATE)
								   .queryParam(UPLOAD_TYPE_PARAM, UPLOAD_TYPE_VALUE)
								   .queryParam(FILE_NAME_PARAM, objectId)
								   .build(config.getBucketName());
	}

	public URI downloadPath(final String objectId) {
		return UriComponentsBuilder.fromUriString(config.getStorageApiUrl())
								   .path(GET_PATH_TEMPLATE)
								   .queryParam(ALT_PARAM, ALT_VALUE)
								   .build(config.getBucketName(), objectId);
	}
}
