package pl.latusikl.database.services.converters;

import jakarta.inject.Singleton;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.entity.UserEntity;

@Singleton
public class UserEntityToUserDtoConverter {
	public UserDto convert(final UserEntity source) {
		return new UserDto(source.getUserEmail(), null, source.getName(), source.getSurname());
	}
}
