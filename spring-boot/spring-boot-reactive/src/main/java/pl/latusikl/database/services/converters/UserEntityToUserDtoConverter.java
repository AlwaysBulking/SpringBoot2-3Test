package pl.latusikl.database.services.converters;

import org.springframework.stereotype.Component;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.entity.UserEntity;

@Component
public class UserEntityToUserDtoConverter {
	public UserDto convert(final UserEntity source) {
		return new UserDto(source.getUserEmail(), null, source.getName(), source.getSurname());
	}
}
