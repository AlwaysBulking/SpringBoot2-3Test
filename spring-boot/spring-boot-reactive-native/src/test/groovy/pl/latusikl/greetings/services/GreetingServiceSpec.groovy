package pl.latusikl.greetings.services

import pl.latusikl.greetings.config.GreetingsConfig
import pl.latusikl.greetings.dto.GreetingDto
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

@Subject(GreetingService)
class GreetingServiceSpec extends Specification {

	GreetingsConfig greetingsConfig = Stub()
	GreetingService subject = new GreetingService(greetingsConfig)

	def "Should generate single GreetingDto"() {
		given:
		def expectedGreetingDto = new GreetingDto("Hi! Nice to see You here!")

		when:
		def resultMono = subject.generateGreeting()

		then:
		StepVerifier.create(resultMono)
				.expectNext(expectedGreetingDto)
				.verifyComplete()
	}


	def "Should generate single GreetingDto with delay"() {
		given:
		def expectedGreetingDto = new GreetingDto("Hi! Nice to see You here!")
		and:
		int delay = 5000

		expect:
		StepVerifier.withVirtualTime(() -> subject.generateGreetingWithDelay(delay))
				.thenAwait(Duration.ofMillis(delay))
				.expectNext(expectedGreetingDto)
				.verifyComplete()
	}

	def "Should generate stream of GreetingDto"() {
		given:
		def expectedGreetingStart = "Hi! Nice to see You here!"
		and:
		greetingsConfig.getSseCount() >> 2

		when:
		def resultFlux = subject.generateGreetingSse()

		then:
		StepVerifier.create(resultFlux)
				.expectNextMatches(data -> data.getValue() == "${expectedGreetingStart} 0")
				.expectNextMatches(data -> data.getValue() == "${expectedGreetingStart} 1")
				.verifyComplete()
	}

}
