package pl.latusikl.database.services.converters

import pl.latusikl.database.entity.UserEntity
import spock.lang.Specification

class UserEntityToUserDtoConverterSpec extends Specification {

	UserEntityToUserDtoConverter subject = new UserEntityToUserDtoConverter()

	def "Should properly convert entity to DTO"() {
		given:
		def email = "email@email.com"
		and:
		def name = "John"
		and:
		def surname = "Doe"
		and:
		def password = "Password123"
		and:
		def id = UUID.randomUUID()
		and:
		def entity = new UserEntity()
		entity.setUserId(id)
		entity.setName(name)
		entity.setSurname(surname)
		entity.setPassword(password)
		entity.setUserEmail(email)

		when:
		def result = subject.convert(entity)

		then:
		result.getPassword() == null
		result.getName() == name
		result.getSurname() == surname
		result.getEmail() == email
	}

}
