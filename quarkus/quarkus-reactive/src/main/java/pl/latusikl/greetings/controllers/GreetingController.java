package pl.latusikl.greetings.controllers;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestSseElementType;
import pl.latusikl.greetings.dto.GreetingDto;
import pl.latusikl.greetings.services.GreetingService;

import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequiredArgsConstructor
@Path("/greetings")
public class GreetingController {

	private final GreetingService greetingService;

	@GET
	@Path("/single")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<RestResponse<GreetingDto>> greeting() {
		return greetingService.generateGreeting()
							  .map(RestResponse::ok);
	}

	@GET
	@Path("/single/{delayInMilliseconds}")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<RestResponse<GreetingDto>> greetingWithDelay(
			@PathParam("delayInMilliseconds") @Min(value = 1, message = "Delay must be greater than 0.") final int delayInMilliseconds) {
		return greetingService.generateGreetingWithDelay(delayInMilliseconds)
							  .map(RestResponse::ok);
	}

	@GET
	@Path("/sse")
	@RestSseElementType(MediaType.APPLICATION_JSON)
	@Produces(MediaType.SERVER_SENT_EVENTS)
	@ResponseStatus(200)
	public Multi<GreetingDto> greetingSse() {
		return greetingService.generateGreetingSse();
	}
}
