package pl.latusikl.database.services.converters;

import jakarta.inject.Singleton;
import pl.latusikl.database.dto.UserCreationResponseDto;
import pl.latusikl.database.entity.UserEntity;

@Singleton
public class UserEntityToUserCreationResponseDtoConverter {

	private static final String LINK_TEMPLATE = "/api/users/%s";

	public UserCreationResponseDto convert(final UserEntity source) {
		final var id = source.getUserId();
		return new UserCreationResponseDto(id, String.format(LINK_TEMPLATE, id));
	}
}
