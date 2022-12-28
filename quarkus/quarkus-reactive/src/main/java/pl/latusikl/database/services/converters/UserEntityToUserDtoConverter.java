package pl.latusikl.database.services.converters;

import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserEntityToUserDtoConverter {
	public UserDto convert(final UserEntity source) {
		return new UserDto(source.getUserEmail(), null, source.getName(), source.getSurname());
	}
}
