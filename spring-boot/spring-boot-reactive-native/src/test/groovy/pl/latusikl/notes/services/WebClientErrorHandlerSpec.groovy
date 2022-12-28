package pl.latusikl.notes.services

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

import java.time.Duration

class WebClientErrorHandlerSpec extends Specification {

	static TIMEOUT = Duration.ofSeconds(1)
	def subject = new WebClientErrorHandler()

	def "Should return mono response with error inside"() {
		given:
		def clientResponse = Stub(ClientResponse.class)
		and:
		def status = HttpStatus.INTERNAL_SERVER_ERROR
		and:
		def internalMessage = "Internal message"
		and:
		def externalMessage = "External message"

		when:
		subject.handleErrorStatus(clientResponse, status, externalMessage, internalMessage).block(TIMEOUT)

		then:
		def exception = thrown ResponseStatusException
		exception.getStatus() == status
		exception.getMessage().contains(externalMessage)
	}

}
