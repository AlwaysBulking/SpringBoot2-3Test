package pl.latusikl.database.services.converters;

import org.springframework.stereotype.Component;
import pl.latusikl.database.dto.UserDto;
import pl.latusikl.database.entity.UserEntity;

@Component
public class UserDtoToUserEntityConverter {
	public UserEntity convert(final UserDto source) {
		final var userEntity = new UserEntity();
		userEntity.setUserEmail(source.getEmail());
		userEntity.setName(source.getName());
		userEntity.setSurname(source.getSurname());
		userEntity.setPassword(source.getPassword());
		return userEntity;
	}
}
