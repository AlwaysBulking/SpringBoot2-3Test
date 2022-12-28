package pl.latusikl.sorting.controllers

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import pl.latusikl.sorting.dto.SortingRequestDto
import pl.latusikl.sorting.services.SortingService
import pl.latusikl.sorting.services.models.SortingStrategy
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest(controllers = SortingController.class)
class SortingControllerSpec extends Specification {

	@Autowired
	private WebTestClient webTestClient

	@SpringBean
	SortingService sortingService = Mock()

	def "Should sort numbers using default direction query param"() {
		given:
		def arrayToSort = [5, 20, 10, 1, -6] as List<Integer>
		and:
		def requestBody = new SortingRequestDto(arrayToSort, SortingStrategy.HEAP_SORT)
		and:
		def sortedArray = [-6, 1, 5, 10, 20] as int[]
		and:
		1 * sortingService.sortAscending({ it.strategy == SortingStrategy.HEAP_SORT && it.data == arrayToSort }) >> Mono.just(sortedArray)

		when:
		def response = webTestClient.post()
				.uri("/sorters/sort")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.exchange()

		then:
		0 * sortingService.sortDescending(_)

		response.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
	}

	def "Should sort numbers using passed direction"() {
		given:
		def arrayToSort = [5, 20, 10, 1, -6] as List<Integer>
		and:
		def requestBody = new SortingRequestDto(arrayToSort, SortingStrategy.HEAP_SORT)
		and:
		def sortedArray = [-6, 1, 5, 10, 20] as int[]
		and:
		1 * sortingService.sortDescending({ it.strategy == SortingStrategy.HEAP_SORT && it.data == arrayToSort }) >> Mono.just(sortedArray)

		when:
		def response = webTestClient.post()
				.uri("/sorters/sort?direction=DESCENDING")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.exchange()

		then:
		0 * sortingService.sortAscending(_)

		response.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
	}

}
