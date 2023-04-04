package pl.latusikl.database.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
	@NotEmpty
	private final String email;
	@NotEmpty
	private final String password;
	@NotEmpty
	private final String name;
	@NotEmpty
	private final String surname;

}
