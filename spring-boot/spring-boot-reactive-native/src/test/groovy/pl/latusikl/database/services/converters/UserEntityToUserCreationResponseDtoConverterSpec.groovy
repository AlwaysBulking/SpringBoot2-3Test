package pl.latusikl.database.services.converters

import pl.latusikl.database.entity.UserEntity
import spock.lang.Specification

class UserEntityToUserCreationResponseDtoConverterSpec extends Specification {

	UserEntityToUserCreationResponseDtoConverter subject = new UserEntityToUserCreationResponseDtoConverter()

	def "Should convert entity to the response for creation of a user"() {
		given:
		def id = UUID.randomUUID()
		and:
		def userEntity = new UserEntity()
		userEntity.setUserId(id)

		when:
		def result = subject.convert(userEntity)

		then:
		result.getId() == id
		result.getLink() == "/api/users/$id"
	}

}
