package pl.latusikl.notes.services

import spock.lang.Specification

class NoteDecryptionServiceSpec extends Specification {

	NoteDecryptionService subject = new NoteDecryptionService()

	def "Should properly encode note value"() {
		given:
		def expectedNoteValue = "Sample note value"
		and:
		def encryptedNoteValue = "T8owr_Rat8UkdSuRm_aTOiHxaLF1pZw4C5YVG8Mhay0="
		and:
		def base64SecretKey = "ddBLE2W5IsiQExVQcTdQEXmHRZR016cD1FDyqrtKZWI="
		and:
		def base64InitVector = "oinQcSKc9VnGNFloru1rSQ=="

		when:
		def result = subject.decrypt(encryptedNoteValue, "${base64SecretKey}:${base64InitVector}")

		then:
		result.isPresent()
		result.get() == expectedNoteValue
	}
}
