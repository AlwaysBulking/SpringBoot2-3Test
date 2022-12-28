package pl.latusikl.notes.services


import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration

class GoogleApiServiceSpec extends Specification {

	Paths paths = Stub()
	WebClientErrorHandler webClientErrorHandler = Mock()
	GoogleTokenProvider tokenProvider = Mock()
	GoogleApiService subject


	static TIMEOUT = Duration.ofSeconds(1)
	static TOKEN = "token123"
	static UPLOAD_URI = URI.create("https://storage.bucket.com")
	static DOWNLOAD_URI = URI.create("https://storage.bucket.com/${ID}")
	static ID = "id123"

	def "Should send note content to bucket"() {
		given:
		String noteContent = "note1234"
		and:
		ClientRequest clientRequest
		and:
		ExchangeFunction exchangeFunction = {
			it ->
				clientRequest = it
				return Mono.just(ClientResponse.create(HttpStatus.OK)
						.header('content-type', 'application/json')
						.body("{\"name\": \"${ID}\"}")
						.build())
		}
		and:
		tokenProvider.getToken() >> Mono.just(TOKEN)
		and:
		paths.uploadPath(ID) >> UPLOAD_URI

		when:
		subject(exchangeFunction).postContentToBucket(noteContent, ID).block(TIMEOUT)

		then:
		clientRequest != null
		clientRequest.method() == HttpMethod.POST
		clientRequest.headers().getContentType() == MediaType.TEXT_PLAIN
		clientRequest.headers().get("Authorization").get(0) == TOKEN
		clientRequest.url().toString() == UPLOAD_URI.toString()
	}

	def "Should fetch note content from storage"() {
		given:
		ClientRequest clientRequest
		and:
		ExchangeFunction exchangeFunction = {
			it ->
				clientRequest = it
				return Mono.just(ClientResponse.create(HttpStatus.OK)
						.header('content-type', 'text/plain')
						.body("Note content here")
						.build())
		}
		and:
		tokenProvider.getToken() >> Mono.just(TOKEN)
		and:
		paths.downloadPath(ID) >> DOWNLOAD_URI

		when:
		subject(exchangeFunction).getContentFromBucket(ID).block(TIMEOUT)

		then:
		clientRequest != null
		clientRequest.method() == HttpMethod.GET
		clientRequest.headers().getAccept().get(0) == MediaType.TEXT_PLAIN
		clientRequest.headers().get("Authorization").get(0) == TOKEN
		clientRequest.url().toString() == DOWNLOAD_URI.toString()
	}

	def subject(ExchangeFunction exchangeFunction) {
		def webClient = WebClient.builder()
				.exchangeFunction(exchangeFunction)
				.build()
		subject = new GoogleApiService(paths, webClient, webClientErrorHandler, tokenProvider)
	}
}
