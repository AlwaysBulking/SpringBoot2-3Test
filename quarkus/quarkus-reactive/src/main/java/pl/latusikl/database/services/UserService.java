package pl.latusikl.database.services;

import io.smallrye.mutiny.Uni;
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
import pl.latusikl.common.ResponseStatusException;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final UserEntityToUserCreationResponseDtoConverter userEntityToUserCreationResponseDtoConverter;
	private final UserDtoToUserEntityConverter userDtoToUserEntityConverter;
	private final UserEntityToUserDtoConverter userEntityToUserDtoConverter;

	public Uni<UserCreationResponseDto> createUser(final UserDto userDto) {
		return userRepository.existsByUserEmail(userDto.getEmail())
							 .onItem()
							 .invoke(this::throwErrorIfUserWithEmailAlreadyExist)
							 .map(discardValue -> userDtoToUserEntityConverter.convert(userDto))
							 .flatMap(userRepository::persistAndFlush)
							 .map(userEntityToUserCreationResponseDtoConverter::convert);
	}

	private void throwErrorIfUserWithEmailAlreadyExist(final Boolean value) {
		if (value) {
			throw new ResponseStatusException("User with provided e-mail already exist.", 400);
		}
	}

	public Uni<Void> deleteUser(final UUID userId) {
		return userRepository.deleteByUserId(userId);
	}

	public Uni<UserDto> getUser(final UUID userId) {
		return userRepository.findById(userId)
							 .map(userEntityToUserDtoConverter::convert);
	}

	public Uni<Void> putUser(final UserPutDto userDto, final UUID userId) {
		return updateUser(this::updateUserData, userId, userDto);
	}

	private UserEntity updateUserData(final UserEntity userEntity, final UserPutDto userPutDto) {
		userEntity.setSurname(userPutDto.getSurname());
		userEntity.setName(userPutDto.getName());
		userEntity.setPassword(userPutDto.getPassword());
		return userEntity;
	}

	public Uni<Void> patchUser(final UserPatchDto userDto, final UUID userId) {
		return updateUser(this::patchUserData, userId, userDto);
	}

	private UserEntity patchUserData(final UserEntity userEntity, final UserPatchDto userPatchDto) {
		userEntity.setSurname(requireNonNullElse(userPatchDto.getSurname(), userEntity.getSurname()));
		userEntity.setName(requireNonNullElse(userPatchDto.getName(), userEntity.getName()));
		userEntity.setPassword(requireNonNullElse(userPatchDto.getPassword(), userEntity.getPassword()));
		return userEntity;
	}

	private <T> Uni<Void> updateUser(final BiFunction<UserEntity, T, UserEntity> mappingFunction, final UUID userId, final T dto) {
		return userRepository.findById(userId)
							 .onItem()
							 .invoke(this::throwErrorIfUserNotFound)
							 .map(userEntity -> mappingFunction.apply(userEntity, dto))
							 .flatMap(userRepository::persistAndFlush)
							 .replaceWithVoid();
	}

	private void throwErrorIfUserNotFound(final UserEntity user) {
		if (isNull(user)) {
			throw new ResponseStatusException("User does not exist", 404);
		}
	}
}
