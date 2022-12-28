package pl.latusikl.database.services.converters

import pl.latusikl.database.dto.UserDto
import spock.lang.Specification

class UserDtoToUserEntityConverterSpec extends Specification {

	UserDtoToUserEntityConverter subject = new UserDtoToUserEntityConverter()

	def "Should convert DTO to entity."() {
		given:
		def email = "email@email.com"
		and:
		def name = "John"
		and:
		def surname = "Doe"
		and:
		def password = "Password123"
		and:
		def userDto = new UserDto(email, password, name, surname)

		when:
		def result = subject.convert(userDto)

		then:
		result.getUserId() == null
		result.getUserEmail() == email
		result.getName() == name
		result.getSurname() == surname
		result.getPassword() == password
	}
}
