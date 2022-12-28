package pl.latusikl.notes.services


import pl.latusikl.notes.dto.StorageResponseDto
import pl.latusikl.notes.services.model.Note
import reactor.util.function.Tuples
import spock.lang.Specification

class StorageResponseDtoToQRCodeResponseDtoConverterSpec extends Specification {

	StorageResponseDtoNoteTupleToNoteResponseDtoConverter subject = new StorageResponseDtoNoteTupleToNoteResponseDtoConverter(20)

	def "Should properly convert StorageResponseDto to NoteResponseDto"() {
		given:
		def noteId = "id123"
		and:
		def noteKey = "key123"
		and:
		def storageResponseDto = new StorageResponseDto(noteId, null)
		and:
		def noteModel = new Note("Note Content", noteKey)

		when:
		def result = subject.convert(Tuples.of(storageResponseDto, noteModel))

		then:
		result.getId() == noteId
		result.getLink() == "/api/notes/${noteId}?noteKey=${noteKey}"
		result.getKey() == noteKey
	}

}
