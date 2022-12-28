package pl.latusikl.notes.services;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import pl.latusikl.notes.dto.NoteDto;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.dto.StorageResponseDto;
import pl.latusikl.notes.services.model.Note;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class NotesService {

	private final NoteEncryptionService noteEncryptionService;
	private final NoteDecryptionService noteDecryptionService;
	private final GoogleApiService googleApiService;
	private final StorageResponseDtoNoteTupleToNoteResponseDtoConverter storageResponseDtoToQRCodeResponseDtoConverter;

	public Mono<NoteResponseDto> createNote(final NoteDto noteDto) {
		return encryptNote(noteDto.getNoteValue()).zipWith(uuid())
												  .flatMap(this::saveNote)
												  .map(storageResponseDtoToQRCodeResponseDtoConverter::convert);
	}

	private Mono<Note> encryptNote(final String noteContent) {
		return Mono.fromCallable(() -> noteEncryptionService.encrypt(noteContent)
															.orElseThrow(
																	() -> new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
																								  "Unable to encrypt note.")))
				   .subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<String> uuid() {
		return Mono.fromCallable(() -> UUID.randomUUID()
										   .toString())
				   .subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<Tuple2<StorageResponseDto, Note>> saveNote(final Tuple2<Note, String> noteWithIdTuple) {
		return googleApiService.postContentToBucket(noteWithIdTuple.getT1()
																   .getNoteContent(), noteWithIdTuple.getT2())
							   .map(storageResponseDto -> Tuples.of(storageResponseDto, noteWithIdTuple.getT1()));
	}

	public Mono<String> getNote(final String noteId, final String noteKey) {
		return googleApiService.getContentFromBucket(noteId)
							   .flatMap(securedNote -> decryptNote(securedNote, noteKey));

	}

	private Mono<String> decryptNote(final String securedNote, final String noteKey) {
		return Mono.fromCallable(() -> noteDecryptionService.decrypt(securedNote, noteKey)
															.orElseThrow(
																	() -> new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
																								  "Unable to decrypt note.")))
				   .subscribeOn(Schedulers.boundedElastic());
	}
}
