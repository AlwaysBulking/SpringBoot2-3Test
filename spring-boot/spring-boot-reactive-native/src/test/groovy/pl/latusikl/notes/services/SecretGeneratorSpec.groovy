package pl.latusikl.notes.services

import spock.lang.Specification

class SecretGeneratorSpec extends Specification {

	def "Should properly initialize object"() {
		expect:
		new SecretGenerator() != null
	}

	def "Should generate init vector with predefined size"() {
		given:
		def subject = new SecretGenerator()
		when:
		def result = subject.generateInitializationVector()
		then:
		result.getIV().length == 16
	}

	def "Should generate proper AES secret key"() {
		given:
		def subject = new SecretGenerator()
		when:
		def result = subject.generateKey()
		then:
		result != null
		result.getAlgorithm() == "AES"
	}
}
