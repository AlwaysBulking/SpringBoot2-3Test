package pl.latusikl.database.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import pl.latusikl.database.dto.UserCreationResponseDto;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.dto.UserPatchDto;
import pl.latusikl.database.dto.UserPutDto;
import pl.latusikl.database.services.UserService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@Controller("/users")
public class UserController {

	private final UserService userService;

	@Post(produces = MediaType.APPLICATION_JSON)
	@Status(HttpStatus.CREATED)
	public Mono<UserCreationResponseDto> addUser(@Body @Valid final UserDto userDto) {
		return userService.createUser(userDto);
	}

	@Get(uri = "/{userId}", produces = MediaType.APPLICATION_JSON)
	public Mono<MutableHttpResponse<UserDto>> getUser(@PathVariable final UUID userId) {
		return userService.getUser(userId)
						  .map(HttpResponse::ok)
						  .defaultIfEmpty(HttpResponse.notFound());

	}

	@Delete(uri = "/{userId}")
	public Mono<HttpResponse<Void>> deleteUser(@PathVariable final UUID userId) {
		return userService.deleteUser(userId)
						  .thenReturn(HttpResponse.noContent());
	}

	@Status(HttpStatus.OK)
	@Put(uri = "/{userId}")
	public Mono<Void> putUser(@PathVariable final UUID userId, @Body final UserPutDto userPutDto) {
		return userService.putUser(userPutDto, userId);
	}

	@Status(HttpStatus.OK)
	@Patch(uri = "/{userId}")
	public Mono<Void> patchUser(@PathVariable final UUID userId, @Body final UserPatchDto userDto) {
		return userService.patchUser(userDto, userId);
	}
}
