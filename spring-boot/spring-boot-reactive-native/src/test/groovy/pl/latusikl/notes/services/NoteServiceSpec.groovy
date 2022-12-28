package pl.latusikl.notes.services


import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import pl.latusikl.notes.dto.NoteDto
import pl.latusikl.notes.dto.NoteResponseDto
import pl.latusikl.notes.dto.StorageResponseDto
import pl.latusikl.notes.services.model.Note
import reactor.core.publisher.Mono
import reactor.util.function.Tuples
import spock.lang.Specification

import java.time.Duration

class NoteServiceSpec extends Specification {


	NoteEncryptionService noteEncryptionService = Mock()
	NoteDecryptionService noteDecryptionService = Mock()
	GoogleApiService googleApiService = Mock()
	StorageResponseDtoNoteTupleToNoteResponseDtoConverter storageResponseDtoToQRCodeResponseDtoConverter = Mock()

	NotesService subject = new NotesService(noteEncryptionService, noteDecryptionService, googleApiService, storageResponseDtoToQRCodeResponseDtoConverter)

	static TIMEOUT = Duration.ofSeconds(1)

	def "Should fetch and decrypt note"() {
		given:
		def encryptedNoteContent = "encryptedNoteContent"
		and:
		def decryptedNote = "decryptedNote"
		and:
		def noteKey = "key123"
		and:
		def id = "id123"
		and:
		1 * googleApiService.getContentFromBucket(id) >> Mono.just(encryptedNoteContent)
		and:
		1 * noteDecryptionService.decrypt(encryptedNoteContent, noteKey) >> Optional.of(decryptedNote)

		when:
		def result = subject.getNote(id, noteKey).block(TIMEOUT)

		then:
		result == decryptedNote
	}

	def "Should throw error when note decryption returned empty optional"() {
		given:
		def encryptedNoteContent = "encryptedNoteContent"
		and:
		def noteKey = "key123"
		and:
		def id = "id123"
		and:
		1 * googleApiService.getContentFromBucket(id) >> Mono.just(encryptedNoteContent)
		and:
		1 * noteDecryptionService.decrypt(encryptedNoteContent, noteKey) >> Optional.empty()

		when:
		subject.getNote(id, noteKey).block(TIMEOUT)

		then:
		def exception = thrown ResponseStatusException
		exception.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
	}

	def "Should encrypt note and post content to bucket"() {
		given:
		def noteValue = "This is note value"
		and:
		def noteDto = new NoteDto(noteValue)
		and:
		def noteKey = "key123"
		and:
		def noteId = "id123"
		and:
		def encryptedNoteValue = "This is encrypted content"
		and:
		def note = new Note(encryptedNoteValue, noteKey)
		and:
		1 * noteEncryptionService.encrypt(noteValue) >> Optional.of(note)
		and:
		def storageResponse = new StorageResponseDto(noteId, null)
		and:
		1 * googleApiService.postContentToBucket(encryptedNoteValue, _) >> Mono.just(storageResponse)
		and:
		def noteResponseDto = new NoteResponseDto(noteId, "Some date", "key", "someLink")
		and:
		1 * storageResponseDtoToQRCodeResponseDtoConverter.convert(Tuples.of(storageResponse, note)) >> noteResponseDto

		when:
		def responseDto = subject.createNote(noteDto).block(TIMEOUT)

		then:
		responseDto == noteResponseDto
	}

	def "Should throw error when note encryption returned empty optional"() {
		given:
		def noteValue = "This is note value"
		and:
		def noteDto = new NoteDto(noteValue)
		and:
		1 * noteEncryptionService.encrypt(noteValue) >> Optional.empty()

		when:
		subject.createNote(noteDto).block(TIMEOUT)

		then:
		def exception = thrown ResponseStatusException
		exception.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
	}

}
