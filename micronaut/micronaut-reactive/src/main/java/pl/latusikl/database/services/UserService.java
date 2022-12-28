package pl.latusikl.database.services;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.latusikl.database.dto.UserCreationResponseDto;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.dto.UserPatchDto;
import pl.latusikl.database.dto.UserPutDto;
import pl.latusikl.database.entity.UserEntity;
import pl.latusikl.database.services.converters.UserDtoToUserEntityConverter;
import pl.latusikl.database.services.converters.UserEntityToUserCreationResponseDtoConverter;
import pl.latusikl.database.services.converters.UserEntityToUserDtoConverter;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserDtoToUserEntityConverter userDtoToUserEntityConverter;
	private final UserEntityToUserCreationResponseDtoConverter userEntityToUserCreationResponseDtoConverter;
	private final UserEntityToUserDtoConverter userEntityToUserDtoConverter;

	public Mono<UserCreationResponseDto> createUser(final UserDto userDto) {
		return userRepository.existsByUserEmail(userDto.getEmail())
							 .doOnSuccess(this::throwErrorIfUserWithEmailAlreadyExist)
							 .thenReturn(userDtoToUserEntityConverter.convert(userDto))
							 .flatMap(userRepository::save)
							 .map(userEntityToUserCreationResponseDtoConverter::convert);
	}

	private void throwErrorIfUserWithEmailAlreadyExist(final Boolean value) {
		if (value) {
			throw new HttpStatusException(HttpStatus.BAD_REQUEST, "User with provided e-mail already exist.");
		}
	}

	public Mono<Void> deleteUser(final UUID userId) {
		return userRepository.deleteById(userId)
							 .then();
	}

	public Mono<UserDto> getUser(final UUID userId) {
		return userRepository.findById(userId)
							 .mapNotNull(userEntityToUserDtoConverter::convert);
	}

	public Mono<Void> putUser(final UserPutDto userDto, final UUID userId) {
		return updateUser(this::updateUserData, userId, userDto);
	}

	private UserEntity updateUserData(final UserEntity userEntity, final UserPutDto userPutDto) {
		userEntity.setSurname(userPutDto.getSurname());
		userEntity.setName(userPutDto.getName());
		userEntity.setPassword(userPutDto.getPassword());
		return userEntity;
	}

	public Mono<Void> patchUser(final UserPatchDto userDto, final UUID userId) {
		return updateUser(this::patchUserData, userId, userDto);
	}

	private UserEntity patchUserData(final UserEntity userEntity, final UserPatchDto userPatchDto) {
		userEntity.setSurname(requireNonNullElse(userPatchDto.getSurname(), userEntity.getSurname()));
		userEntity.setName(requireNonNullElse(userPatchDto.getName(), userEntity.getName()));
		userEntity.setPassword(requireNonNullElse(userPatchDto.getPassword(), userEntity.getPassword()));
		return userEntity;
	}

	private <T> Mono<Void> updateUser(final BiFunction<UserEntity, T, UserEntity> mappingFunction, final UUID userId, final T dto) {
		return userRepository.findById(userId)
							 .doOnSuccess(this::throwErrorIfUserNotFound)
							 .map(userEntity -> mappingFunction.apply(userEntity, dto))
							 .flatMap(userRepository::update)
							 .then();
	}

	private void throwErrorIfUserNotFound(final UserEntity user) {
		if (isNull(user)) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "User does not exist");
		}
	}
}
