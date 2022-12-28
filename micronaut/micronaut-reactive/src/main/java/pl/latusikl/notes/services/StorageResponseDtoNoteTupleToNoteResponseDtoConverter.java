package pl.latusikl.notes.services;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.dto.StorageResponseDto;
import pl.latusikl.notes.services.model.Note;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Singleton
public class StorageResponseDtoNoteTupleToNoteResponseDtoConverter {

	private static final String LINK_TEMPLATE = "/api/notes/%s?noteKey=%s";
	private final int daysTillRemoval;

	public StorageResponseDtoNoteTupleToNoteResponseDtoConverter(
			@Value("${code.days-till-removal}") final int daysTillRemoval) {this.daysTillRemoval = daysTillRemoval;}

	public NoteResponseDto convert(final Tuple2<StorageResponseDto, Note> source) {
		final var storageResponse = source.getT1();
		final var noteModel = source.getT2();
		final var noteLink = String.format(LINK_TEMPLATE, storageResponse.getName(), noteModel.getNoteKey());
		return new NoteResponseDto(storageResponse.getName(), getRemovalDate(), noteModel.getNoteKey(), noteLink);
	}

	private String getRemovalDate() {
		return LocalDateTime.now(ZoneId.of("UTC"))
							.plusDays(daysTillRemoval)
							.toString();
	}
}
