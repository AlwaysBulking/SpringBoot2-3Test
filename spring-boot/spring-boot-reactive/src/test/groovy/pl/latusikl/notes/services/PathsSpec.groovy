package pl.latusikl.notes.services


import pl.latusikl.notes.config.GoogleApiConfig
import spock.lang.Specification

class PathsSpec extends Specification {

	GoogleApiConfig config = Stub()
	Paths subject = new Paths(config)
	static BUCKET_NAME = "bucket123"
	static API_URL = "https://storage.api.com"

	def "Should return proper upload URI"() {
		given:
		def objectId = "id123"
		and:
		config.getBucketName() >> BUCKET_NAME
		and:
		config.getStorageApiUrl() >> API_URL

		when:
		def result = subject.uploadPath(objectId)

		then:
		result.toString() == "$API_URL/upload/storage/v1/b/$BUCKET_NAME/o?uploadType=media&name=${objectId}"
	}

	def "Should return proper download URI"() {
		given:
		def objectId = "id123"
		and:
		config.getBucketName() >> BUCKET_NAME
		and:
		config.getStorageApiUrl() >> API_URL

		when:
		def result = subject.downloadPath(objectId)

		then:
		result.toString() == "$API_URL/storage/v1/b/$BUCKET_NAME/o/${objectId}?alt=media"
	}


}
