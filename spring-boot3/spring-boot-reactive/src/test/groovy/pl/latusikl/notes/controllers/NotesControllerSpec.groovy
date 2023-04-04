package pl.latusikl.notes.controllers

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import pl.latusikl.notes.dto.NoteDto
import pl.latusikl.notes.dto.NoteResponseDto
import pl.latusikl.notes.services.NotesService
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDateTime

@WebFluxTest(controllers = NotesController.class)
class NotesControllerSpec extends Specification {

	@Autowired
	WebTestClient webTestClient

	@SpringBean
	NotesService notesService = Mock()

	static String ID = UUID.randomUUID().toString()
	static String KEY = "sampleKey"
	static String EXPIRATION = LocalDateTime.now().toString()
	static String LINK = "/api/codes/${ID}?noteKey=${KEY}"
	static String NOTE_VALUE = "testValue123"

	def "Should return generated note id and key"(){
		given:
		def noteResponseDto = new NoteResponseDto(ID, EXPIRATION,KEY, LINK)
		and:
		def noteDto = new NoteDto(NOTE_VALUE)
		and:
		1 * notesService.createNote(noteDto) >> Mono.just(noteResponseDto)

		when:
		def response = webTestClient.post()
				.uri("/notes")
				.bodyValue(noteDto)
				.exchange()

		then:
		response.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody(NoteResponseDto.class)
				.isEqualTo(noteResponseDto)
	}

	def "Should return note by ID"() {
		given:
		def decryptedNote = "decryptedNote"
		and:
		1 * notesService.getNote(ID,KEY) >> Mono.just(decryptedNote)

		when:
		def response = webTestClient.get()
				.uri("/notes/${ID}?noteKey=${KEY}".toString())
				.exchange()

		then:
		response.expectStatus()
				.isOk()
				.expectHeader()
				.contentType("text/plain;charset=UTF-8")
	}

}
