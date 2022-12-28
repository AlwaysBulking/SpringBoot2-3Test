package pl.latusikl.notes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class StorageResponseDto {
	@JsonProperty
	private final String name;
	@JsonProperty
	private final String retentionExpirationTime;
}

