package pl.latusikl.notes.config


import spock.lang.Specification

class GoogleApiConfigSpec extends Specification {

	GoogleApiConfig subject

	def setup() {
		subject = new GoogleApiConfig()
	}

	def "Should change comma to the space delimiter"() {
		given:
		def scope1 = "scope1"
		and:
		def scope2 = "scope2"
		and:
		subject.setScopes("${scope1},${scope2}")

		when:
		def result = subject.getScopes()

		then:
		result == "${scope1} ${scope2}"
	}

	def "Should not change scope parameter if no comma splitting scopes"() {
		given:
		def scope1 = "scope1"
		and:
		subject.setScopes(scope1)

		when:
		def result = subject.getScopes()

		then:
		result == scope1
	}

}
