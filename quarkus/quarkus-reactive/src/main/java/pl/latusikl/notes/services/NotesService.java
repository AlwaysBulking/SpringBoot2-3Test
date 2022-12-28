package pl.latusikl.notes.services;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.RequiredArgsConstructor;
import pl.latusikl.notes.dto.NoteDto;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.dto.StorageResponseDto;
import pl.latusikl.notes.services.model.Note;
import pl.latusikl.common.ResponseStatusException;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class NotesService {

	private final NoteEncryptionService noteEncryptionService;
	private final NoteDecryptionService noteDecryptionService;
	private final GoogleApiService googleApiService;
	private final StorageResponseDtoNoteTupleToNoteResponseDtoConverter storageResponseDtoNoteTupleToNoteResponseDtoConverter;

	public Uni<NoteResponseDto> createNote(final NoteDto noteDto) {
		return Uni.combine()
				  .all()
				  .unis(encryptNote(noteDto.getNoteValue()), uuid())
				  .asTuple()
				  .flatMap(this::saveNote)
				  .map(storageResponseDtoNoteTupleToNoteResponseDtoConverter::convert);
	}

	private Uni<Note> encryptNote(final String noteContent) {
		return Uni.createFrom()
				  .item(() -> noteEncryptionService.encrypt(noteContent)
												   .orElseThrow(() -> new ResponseStatusException("Unable to encrypt note.",
																								  HttpResponseStatus.INTERNAL_SERVER_ERROR.code())))
				  .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}

	private Uni<String> uuid() {
		return Uni.createFrom()
				  .item(() -> UUID.randomUUID()
								  .toString())
				  .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}

	private Uni<Tuple2<StorageResponseDto, Note>> saveNote(final Tuple2<Note, String> noteWithIdTuple) {
		return googleApiService.postContentToBucket(noteWithIdTuple.getItem1()
																   .getNoteContent(), noteWithIdTuple.getItem2())
							   .map(storageResponseDto -> Tuple2.of(storageResponseDto, noteWithIdTuple.getItem1()));
	}

	public Uni<String> getNote(final String noteId, final String noteKey) {
		return googleApiService.getContentFromBucket(noteId)
							   .flatMap(securedNote -> decryptNote(securedNote, noteKey));

	}

	private Uni<String> decryptNote(final String securedNote, final String noteKey) {
		return Uni.createFrom()
				  .item(() -> noteDecryptionService.decrypt(securedNote, noteKey)
												   .orElseThrow(() -> new ResponseStatusException("Unable to decrypt note.",
																								  HttpResponseStatus.INTERNAL_SERVER_ERROR.code())))
				  .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}
}
