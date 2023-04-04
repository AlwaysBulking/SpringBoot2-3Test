package pl.latusikl.sorting.services.sorters.heap

import spock.lang.Specification
import spock.lang.Subject

@Subject(MaxIntHeap)
class MaxIntHeapSpec extends Specification {

	MaxIntHeap subject;

	def setup() {
		subject = new MaxIntHeap();
	}

	def "Should return proper heap size"() {
		given:
		def elementsToAdd = [1, 5, 7, 9]
		and:
		def expectedSize = elementsToAdd.size()

		when:
		elementsToAdd.forEach(item -> subject.add(item))
		def heapSize = subject.getHeapSize()

		then:
		heapSize == expectedSize
	}

	def "Should add elements to heap and store them in proper order"() {
		given:
		def heapElements = [10, 20, 11, 7, -5, 50]

		when:
		heapElements.forEach(item -> subject.add(item))

		then:
		subject.items[0] == 50
		subject.items[1] == 10
		subject.items[2] == 20
		subject.items[3] == 7
		subject.items[4] == -5
		subject.items[5] == 11

	}

	def "Should always pop the biggest element in the heap"(){
		given:
		def heapElements = [200,100,500]

		and:
		heapElements.forEach(item -> subject.add(item))

		when:
		def results = [subject.pop(),subject.pop(),subject.pop()]

		then:
		results == [500,200,100]
	}

}
