package pl.latusikl.database.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.latusikl.database.dto.UserCreationResponseDto;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.dto.UserPatchDto;
import pl.latusikl.database.dto.UserPutDto;
import pl.latusikl.database.services.UserService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<UserCreationResponseDto> addUser(@Valid @RequestBody final UserDto userDto) {
		return userService.createUser(userDto);
	}

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<UserDto>> getUser(@PathVariable final UUID userId) {
		return userService.getUser(userId)
						  .map(ResponseEntity::ok)
						  .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

	@DeleteMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> deleteUser(@PathVariable final UUID userId) {
		return userService.deleteUser(userId)
						  .thenReturn(ResponseEntity.noContent()
													.build());
	}

	@PutMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> putUser(@PathVariable final UUID userId, @RequestBody final UserPutDto userPutDto) {
		return userService.putUser(userPutDto, userId);
	}

	@PatchMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> patchUser(@PathVariable final UUID userId, @RequestBody final UserPatchDto userDto) {
		return userService.patchUser(userDto, userId);
	}
}
