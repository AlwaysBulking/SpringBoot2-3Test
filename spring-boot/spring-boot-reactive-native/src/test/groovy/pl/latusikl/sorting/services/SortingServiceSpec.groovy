package pl.latusikl.sorting.services

import pl.latusikl.sorting.dto.SortingRequestDto
import pl.latusikl.sorting.dto.SortingResponseDto
import pl.latusikl.sorting.services.models.SortingStrategy
import pl.latusikl.sorting.services.sorters.Sorter
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject(SortingService)
class SortingServiceSpec extends Specification {

	Sorter heapSort = Mock()
	Sorter quickSort = Mock()
	Sorter mergeSort = Mock()

	SortingService subject

	def setup() {
		heapSort.getSortingStrategy() >> SortingStrategy.HEAP_SORT
		quickSort.getSortingStrategy() >> SortingStrategy.QUICKSORT
		mergeSort.getSortingStrategy() >> SortingStrategy.MERGE_SORT

		subject = new SortingService([heapSort, mergeSort, quickSort])
	}

	@Unroll
	def "Should use proper sorting strategy #sortingStrategy to sort ascending"(SortingStrategy sortingStrategy, int[] sorterCalls) {
		given:
		List<Integer> arrayToSort = [8, 4, 5, 6]
		and:
		int[] sortedArray = [4, 5, 6, 8]
		and:
		def sortingRequest = new SortingRequestDto(arrayToSort, sortingStrategy)
		and:
		sorterCalls[0] * mergeSort.sortAscending(arrayToSort) >> sortedArray
		sorterCalls[1] * quickSort.sortAscending(arrayToSort) >> sortedArray
		sorterCalls[2] * heapSort.sortAscending(arrayToSort) >> sortedArray

		when:
		def sortingResultMono = subject.sortAscending(sortingRequest)

		then:
		StepVerifier.create(sortingResultMono)
				.expectNext(new SortingResponseDto(sortedArray))
				.verifyComplete()

		where:
		sortingStrategy            | sorterCalls
		SortingStrategy.MERGE_SORT | [1, 0, 0]
		SortingStrategy.QUICKSORT  | [0, 1, 0]
		SortingStrategy.HEAP_SORT  | [0, 0, 1]
	}

}
