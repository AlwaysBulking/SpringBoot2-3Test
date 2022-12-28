package pl.latusikl.database.controllers;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;
import pl.latusikl.database.dto.UserCreationResponseDto;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.dto.UserPatchDto;
import pl.latusikl.database.dto.UserPutDto;
import pl.latusikl.database.services.UserService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@POST
	@ResponseStatus(201)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<UserCreationResponseDto> addUser(@Valid final UserDto userDto) {
		return userService.createUser(userDto);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{userId}")
	public Uni<RestResponse<UserDto>> getUser(@PathParam("userId") final UUID userId) {
		return userService.getUser(userId)
						  .map(RestResponse::ok)
						  .onFailure()
						  .recoverWithItem(RestResponse.notFound());
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{userId}")
	public Uni<RestResponse<Void>> deleteUser(@PathParam("userId") final UUID userId) {
		return userService.deleteUser(userId)
						  .replaceWith(RestResponse.noContent());

	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{userId}")
	@ResponseStatus(200)
	public Uni<Void> putUser(@PathParam("userId") final UUID userId, @Valid final UserPutDto userPutDto) {
		return userService.putUser(userPutDto, userId);
	}

	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{userId}")
	@ResponseStatus(200)
	public Uni<Void> patchUser(@PathParam("userId") final UUID userId, final UserPatchDto userDto) {
		return userService.patchUser(userDto, userId);
	}
}
