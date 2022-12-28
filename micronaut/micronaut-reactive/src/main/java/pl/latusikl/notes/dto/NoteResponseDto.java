package pl.latusikl.notes.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Introspected
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NoteResponseDto {
	private final String id;
	private final String expireAt;
	private final String key;
	private final String link;
}

