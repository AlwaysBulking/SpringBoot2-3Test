package pl.latusikl.greetings.controllers

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import pl.latusikl.greetings.dto.GreetingDto
import pl.latusikl.greetings.services.GreetingService
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest(controllers = GreetingController.class)
class GreetingControllerSpec extends Specification {

	@SpringBean
	GreetingService greetingService = Mock()

	@Autowired
	WebTestClient webTestClient

	def "Should generate single greeting"() {
		given:
		def greetingDto = new GreetingDto("Hello")
		and:
		1 * greetingService.generateGreeting() >> Mono.just(greetingDto)

		when:
		def receivedResponse = webTestClient.get()
				.uri("/greetings/single")
				.exchange()

		then:
		receivedResponse.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
	}


	def "Should generate single greeting with delay"() {
		given:
		def greetingDto = new GreetingDto("Hello")
		and:
		1 * greetingService.generateGreetingWithDelay(100) >> Mono.just(greetingDto)

		when:
		def receivedResponse = webTestClient.get()
				.uri("/greetings/single/100")
				.exchange()

		then:
		receivedResponse.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
	}
}
