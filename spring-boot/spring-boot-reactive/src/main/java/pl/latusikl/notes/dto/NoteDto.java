package pl.latusikl.notes.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class NoteDto {

	@NotEmpty
	@Size(min = 1, max = 10000)
	private final String noteValue;

	@JsonCreator
	public NoteDto(final String noteValue) {
		this.noteValue = noteValue;
	}
}
