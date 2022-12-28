package pl.latusikl.notes.services.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.latusikl.notes.dto.TokenResponseDto;
import pl.latusikl.notes.services.ResponseErrorExceptionProducer;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey = "google-token-web-client")
public interface GoogleTokenWebClient {

	String TOKEN_PATH = "/token";
	String GRANT_TYPE = "grant_type";
	String ASSERTION = "assertion";

	@ClientExceptionMapper
	static RuntimeException mapFailureToException(final Response response) {
		return ResponseErrorExceptionProducer.createExceptionForErrorResponse(response, "Error occurred during processing.",
																			  "Error occurred when trying to exchange token for Google API.");
	}

	@Path(TOKEN_PATH)
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Uni<TokenResponseDto> getToken(@FormParam(GRANT_TYPE) final String grantType, @FormParam(ASSERTION) final String assertion);
}
