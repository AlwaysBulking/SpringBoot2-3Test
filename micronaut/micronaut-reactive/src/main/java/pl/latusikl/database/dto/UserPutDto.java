package pl.latusikl.database.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Introspected
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class UserPutDto {
	@NotEmpty
	private final String password;
	@NotEmpty
	private final String name;
	@NotEmpty
	private final String surname;
}
