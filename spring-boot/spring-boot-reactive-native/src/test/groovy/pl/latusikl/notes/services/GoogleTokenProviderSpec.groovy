package pl.latusikl.notes.services

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import pl.latusikl.notes.config.GoogleApiConfig
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration

class GoogleTokenProviderSpec extends Specification {

	private final GoogleApiConfig config = Stub()
	private final GoogleTokenBuilder googleJWTBuilder = Stub()
	private final WebClientErrorHandler errorHandler = Stub()
	static TIMEOUT = Duration.ofSeconds(1)

	def "Should fetch token after construction"() {
		given:
		def tokenAfterExchange = "ajsdnvfaisjnvipuefn"
		and:
		def tokenToExchange = "aodsnvapjrenvpiuenrs"
		and:
		def authUrl = "https://auth.com"
		and:
		ClientRequest clientRequest
		and:
		def webClient = WebClient.builder()
				.exchangeFunction(
						{
							it ->
								clientRequest = it
								return Mono.just(ClientResponse.create(HttpStatus.OK)
										.header('content-type', 'application/json')
										.body("{\"access_token\": \"${tokenAfterExchange}\"}")
										.build())
						}
				)
				.build()
		and:
		googleJWTBuilder.buildTokenForExchange() >> tokenToExchange
		and:
		config.getAuthUrl() >> authUrl

		when:
		def response = new GoogleTokenProvider(config, webClient, googleJWTBuilder, errorHandler).getToken().block(TIMEOUT)

		then:
		response != null
		response == "Bearer $tokenAfterExchange"
		clientRequest != null
		clientRequest.method() == HttpMethod.POST
		clientRequest.url().toString() == authUrl
	}

}
