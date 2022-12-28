package pl.latusikl.notes.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Introspected
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class TokenResponseDto {

	private final String accessToken;

	private final Long expiresIn;

	private final String tokenType;

	@JsonCreator
	public TokenResponseDto(@JsonProperty("access_token") final String accessToken, @JsonProperty("expires_in") final Long expiresIn,
							@JsonProperty("token_type") final String tokenType) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.tokenType = tokenType;
	}
}
