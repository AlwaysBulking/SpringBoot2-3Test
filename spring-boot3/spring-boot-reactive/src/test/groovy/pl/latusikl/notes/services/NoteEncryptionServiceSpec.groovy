package pl.latusikl.notes.services

import spock.lang.Specification

import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class NoteEncryptionServiceSpec extends Specification {

	SecretGenerator secretGenerator = Mock()
	NoteEncryptionService subject = new NoteEncryptionService(secretGenerator)

	def "Should properly encode note value"() {
		given:
		def noteValue = "Sample note value"
		and:
		def expectedEncryptedNoteValue = "T8owr_Rat8UkdSuRm_aTOiHxaLF1pZw4C5YVG8Mhay0="
		and:
		def base64SecretKey = "ddBLE2W5IsiQExVQcTdQEXmHRZR016cD1FDyqrtKZWI="
		and:
		def base64InitVector = "oinQcSKc9VnGNFloru1rSQ=="
		and:
		def secretKey = new SecretKeySpec(Base64.getUrlDecoder().decode(base64SecretKey), "AES")
		and:
		def ivParam = new IvParameterSpec(Base64.getUrlDecoder().decode(base64InitVector))
		and:
		1 * secretGenerator.generateKey() >> secretKey
		and:
		1 * secretGenerator.generateInitializationVector() >> ivParam

		when:
		def result = subject.encrypt(noteValue)

		then:
		result.isPresent()
		result.get().getNoteContent() == expectedEncryptedNoteValue
		result.get().getNoteKey() == "${base64SecretKey}:${base64InitVector}"
	}

}
