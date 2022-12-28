package pl.latusikl;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import pl.latusikl.common.ErrorDto;
import pl.latusikl.common.ResponseStatusException;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import static java.util.Objects.requireNonNullElse;

@ApplicationPath("/api")
@Slf4j
public class QuarkusReactiveApplication extends Application {

	private static final String EXCEPTION_FALLBACK = "Unknown error occurred during processing";

	@ServerExceptionMapper
	Uni<RestResponse<ErrorDto>> handleResponseStatusException(final ResponseStatusException responseStatusException) {
		log.warn("Exception during processing occurred.", responseStatusException);
		return Uni.createFrom()
				  .item(new ErrorDto(requireNonNullElse(responseStatusException.getMessage(), EXCEPTION_FALLBACK)))
				  .map(errorDto -> RestResponse.status(RestResponse.Status.fromStatusCode(responseStatusException.getHttpStatus()),
													   errorDto));
	}
}
