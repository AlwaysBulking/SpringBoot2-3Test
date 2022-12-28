package pl.latusikl.notes.services.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.latusikl.notes.dto.StorageResponseDto;
import pl.latusikl.notes.services.ResponseErrorExceptionProducer;
import pl.latusikl.notes.services.util.CustomHeader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey = "google-web-client")
public interface GoogleWebClient {

	String UPLOAD_PATH_TEMPLATE = "/upload/storage/v1/b/{bucket-name}/o";
	String PATH_BUCKET_NAME_PARAM = "bucket-name";
	String UPLOAD_TYPE_QUERY_PARAM = "uploadType";
	String UPLOAD_TYPE_QUERY_VALUE = "media";
	String FILE_NAME_QUERY_PARAM = "name";
	String GET_PATH_TEMPLATE = "storage/v1/b/{bucket-name}/o/{object-id}";
	String PATH_OBJECT_ID_PARAM = "object-id";
	String ALT_QUERY_PARAM = "alt";
	String ALT_VALUE = "media";

	@ClientExceptionMapper
	static RuntimeException mapFailureToException(final Response response) {
		return ResponseErrorExceptionProducer.createExceptionForErrorResponse(response, "Error occurred unable to process QR Code.",
																			  "Error occurred when trying to send or fetch QRCode using Google API.");
	}

	@POST
	@Path(UPLOAD_PATH_TEMPLATE)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	Uni<StorageResponseDto> postText(final String noteValue, @PathParam(PATH_BUCKET_NAME_PARAM) final String bucketName,
									 @QueryParam(UPLOAD_TYPE_QUERY_PARAM) final String uploadType,
									 @QueryParam(FILE_NAME_QUERY_PARAM) final String fileName,
									 @HeaderParam(CustomHeader.AUTHORIZATION) String Authorization);

	@GET
	@Path(GET_PATH_TEMPLATE)
	Uni<String> getText(@PathParam(PATH_BUCKET_NAME_PARAM) final String bucketName, @PathParam(PATH_OBJECT_ID_PARAM) String objectId,
						@QueryParam(ALT_QUERY_PARAM) String alt, @HeaderParam(CustomHeader.AUTHORIZATION) String Authorization);
}
