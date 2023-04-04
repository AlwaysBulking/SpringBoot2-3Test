package pl.latusikl.sorting.services.sorters

import pl.latusikl.sorting.services.models.SortingStrategy
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject(HeapSort)
class HeapSortSpec extends Specification {

	HeapSort subject = new HeapSort()

	def "Should return proper sorting strategy"(){
		when:
		def result = subject.getSortingStrategy()

		then:
		result == SortingStrategy.HEAP_SORT
	}

	@Unroll
	def "Should sort array ascending"(int[] arrayToSort, int[] expectedResult) {
		when:
		def result = subject.sortAscending(arrayToSort)

		then:
		result == expectedResult

		where:
		arrayToSort                                         | expectedResult
		[4, 2, 1, 9, 7, 5]                                  | [1, 2, 4, 5, 7, 9]
		[1]                                                 | [1]
		[2, 1]                                              | [1, 2]
		[1, 2, 3, 4, 5]                                     | [1, 2, 3, 4, 5]
		[5, 4, 3, 2, 1]                                     | [1, 2, 3, 4, 5]
		[7, -10, -5, 15, 25, 2]                             | [-10, -5, 2, 7, 15, 25]
		[12, 15, 66, -10, 222, 666, 111, 15, -30, 3, 6, 12] | [-30, -10, 3, 6, 12, 12, 15, 15, 66, 111, 222, 666]
		[]                                                  | []
	}

	@Unroll
	def "Should sort array descending"(int[] arrayToSort, int[] expectedResult) {
		when:
		def result = subject.sortDescending(arrayToSort)

		then:
		result == expectedResult

		where:
		arrayToSort             | expectedResult
		[4, 2, 1, 9, 7, 5]      | [9, 7, 5, 4, 2, 1]
		[1]                     | [1]
		[2, 1]                  | [2, 1]
		[1, 2, 3, 4, 5]         | [5, 4, 3, 2, 1]
		[5, 4, 3, 2, 1]         | [5, 4, 3, 2, 1]
		[7, -10, -5, 15, 25, 2] | [25, 15, 7, 2, -5, -10]
		[]                      | []
	}

}

