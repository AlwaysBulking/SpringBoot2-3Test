package pl.latusikl.database.controllers

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import pl.latusikl.database.dto.UserDto
import pl.latusikl.database.dto.UserPatchDto
import pl.latusikl.database.dto.UserPutDto
import pl.latusikl.database.services.UserService
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest(controllers = UserController.class)
class UserControllerSpec extends Specification {

	@Autowired
	WebTestClient client

	@SpringBean
	UserService userService = Mock()

	static EMAIL = "email@email.com"
	static NAME = "John"
	static SURNAME = "Doe"
	static PASSWORD = "password123"
	static ID = UUID.randomUUID()
	static ID_STR = ID.toString()

	def "Should delete user by id"() {
		given:
		1 * userService.deleteUser(ID) >> Mono.empty()

		when:
		def response = client.delete()
				.uri("/users/${ID_STR}")
				.exchange()

		then:
		response.expectStatus()
				.isNoContent()
	}

	def "Should get user by id"() {
		given:
		UserDto userDto = new UserDto(EMAIL, PASSWORD, NAME, SURNAME)

		and:
		1 * userService.getUser(ID) >> Mono.just(userDto)

		when:
		def result = client.get()
				.uri("/users/${ID_STR}")
				.exchange()

		then:
		result.expectStatus()
				.isOk()
				.expectBody(UserDto.class)
				.isEqualTo(userDto)
	}

	def "Should return 404 when user not found"() {
		given:
		1 * userService.getUser(ID) >> Mono.empty()

		when:
		def result = client.get()
				.uri("/users/${ID_STR}")
				.exchange()

		then:
		result.expectStatus()
				.isNotFound()
	}

	def "Should update whole user data"() {
		given:
		def userPutDto = new UserPutDto(PASSWORD, NAME, SURNAME)
		and:
		userService.putUser(userPutDto, ID) >> Mono.empty()

		when:
		def result = client.put()
				.uri("/users/${ID_STR}")
				.body(BodyInserters.fromValue(userPutDto))
				.exchange()

		then:
		result.expectStatus()
				.isOk()
	}

	def "Should partially update  user data"() {
		given:
		def userPutDto = new UserPatchDto(PASSWORD, null, null)
		and:
		userService.patchUser(userPutDto, ID) >> Mono.empty()

		when:
		def result = client.patch()
				.uri("/users/${ID_STR}")
				.body(BodyInserters.fromValue(userPutDto))
				.exchange()

		then:
		result.expectStatus()
				.isOk()
	}

}
