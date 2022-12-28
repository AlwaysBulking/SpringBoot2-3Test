package pl.latusikl.notes.services;

import io.smallrye.mutiny.tuples.Tuple2;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.dto.StorageResponseDto;
import pl.latusikl.notes.services.model.Note;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ApplicationScoped
public class StorageResponseDtoNoteTupleToNoteResponseDtoConverter {

	private static final String LINK_TEMPLATE = "/api/notes/%s?noteKey=%s";
	@ConfigProperty(name = "code.days-till-removal")
	int daysTillRemoval;

	public NoteResponseDto convert(final Tuple2<StorageResponseDto, Note> source) {
		final var storageResponse = source.getItem1();
		final var noteModel = source.getItem2();
		final var noteLink = String.format(LINK_TEMPLATE, storageResponse.getName(), noteModel.getNoteKey());
		return new NoteResponseDto(storageResponse.getName(), getRemovalDate(), noteModel.getNoteKey(), noteLink);
	}

	private String getRemovalDate() {
		return LocalDateTime.now(ZoneId.of("UTC"))
							.plusDays(daysTillRemoval)
							.toString();
	}
}
