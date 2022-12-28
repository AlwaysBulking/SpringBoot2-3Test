package pl.latusikl.database.services

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import pl.latusikl.database.dto.UserCreationResponseDto
import pl.latusikl.database.dto.UserDto
import pl.latusikl.database.dto.UserPatchDto
import pl.latusikl.database.dto.UserPutDto
import pl.latusikl.database.entity.UserEntity
import pl.latusikl.database.services.converters.UserDtoToUserEntityConverter
import pl.latusikl.database.services.converters.UserEntityToUserCreationResponseDtoConverter
import pl.latusikl.database.services.converters.UserEntityToUserDtoConverter
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

class UserServiceSpec extends Specification {

	UserRepository userRepository = Mock()
	UserEntityToUserCreationResponseDtoConverter userEntityToUserCreationResponseDtoConverter = Mock()
	UserDtoToUserEntityConverter userDtoToUserEntityConverter = Mock()
	UserEntityToUserDtoConverter userEntityToUserDtoConverter = Mock()

	UserService subject = new UserService(userRepository, userEntityToUserCreationResponseDtoConverter, userDtoToUserEntityConverter, userEntityToUserDtoConverter)

	static EMAIL = "email@email.com"
	static NAME = "John"
	static NAME_2 = "John2"
	static SURNAME = "Doe"
	static SURNAME_2 = "Doe2"
	static PASSWORD = "password123"
	static PASSWORD_2 = "password1234"
	static ID = UUID.randomUUID()
	static TIMEOUT = Duration.ofSeconds(1)

	def "Should throw error when user with given email already exist"() {
		given:
		def userDto = userDto()
		and:
		userRepository.existsByUserEmail(EMAIL) >> Mono.just(Boolean.TRUE)
		and:
		def userEntity = userEntity()
		and:
		userDtoToUserEntityConverter.convert(userDto) >> userEntity
		when:
		subject.createUser(userDto).block(TIMEOUT)

		then:
		def exception = thrown(ResponseStatusException)
		exception.getStatus() == HttpStatus.BAD_REQUEST
	}

	def "Should create new user"() {
		given:
		def userDto = userDto()
		and:
		userRepository.existsByUserEmail(EMAIL) >> Mono.just(Boolean.FALSE)
		and:
		def userEntity = userEntity()
		and:
		userDtoToUserEntityConverter.convert(userDto) >> userEntity
		and:
		1 * userRepository.save(userEntity) >> Mono.just(userEntity)
		and:
		def userCreationResponseDto = new UserCreationResponseDto(ID, "link")
		and:
		userEntityToUserCreationResponseDtoConverter.convert(userEntity) >> userCreationResponseDto

		when:
		def result = subject.createUser(userDto).block(TIMEOUT)

		then:
		result == userCreationResponseDto
	}

	def "Should delete user"() {
		when:
		subject.deleteUser(ID).block(TIMEOUT)

		then:
		1 * userRepository.deleteById(ID) >> Mono.empty()
	}

	def "Should get user from DB"() {
		given:
		def userDto = userDto()
		and:
		def userEntity = userEntity()
		and:
		userRepository.findById(ID) >> Mono.just(userEntity)
		and:
		userEntityToUserDtoConverter.convert(userEntity) >> userDto

		when:
		def result = subject.getUser(ID).block(TIMEOUT)

		then:
		result == userDto
	}

	def "Should update all user data"() {
		given:
		def userPutDto = userPutDto()
		and:
		def userEntity = userEntity()
		and:
		1 * userRepository.findById(ID) >> Mono.just(userEntity)

		when:
		subject.putUser(userPutDto, ID).block(TIMEOUT)

		then:
		1 * userRepository.save({ it.getName() == NAME_2 && it.getSurname() == SURNAME_2 && it.getPassword() == PASSWORD_2 }) >> Mono.empty()
	}

	@Unroll
	def "Should partial update all user data"(UserPatchDto userPatchDto, String expectedName, String expectedSurname, String expectedPassword) {
		given:
		def userEntity = userEntity()
		and:
		1 * userRepository.findById(ID) >> Mono.just(userEntity)

		when:
		subject.patchUser(userPatchDto, ID).block(TIMEOUT)

		then:
		1 * userRepository.save({ it.getName() == expectedName && it.getSurname() == expectedSurname && it.getPassword() == expectedPassword }) >> Mono.empty()

		where:
		userPatchDto                                    | expectedName | expectedSurname | expectedPassword
		new UserPatchDto(null, null, PASSWORD_2)        | NAME         | SURNAME         | PASSWORD_2
		new UserPatchDto(NAME_2, null, null)            | NAME_2       | SURNAME         | PASSWORD
		new UserPatchDto(null, SURNAME_2, null)         | NAME         | SURNAME_2       | PASSWORD
		new UserPatchDto(NAME_2, SURNAME_2, null)       | NAME_2       | SURNAME_2       | PASSWORD
		new UserPatchDto(NAME_2, null, PASSWORD_2)      | NAME_2       | SURNAME         | PASSWORD_2
		new UserPatchDto(null, SURNAME_2, PASSWORD_2)   | NAME         | SURNAME_2       | PASSWORD_2
		new UserPatchDto(NAME_2, SURNAME_2, PASSWORD_2) | NAME_2       | SURNAME_2       | PASSWORD_2
	}

	def userDto() {
		return new UserDto(EMAIL, PASSWORD, NAME, SURNAME)
	}

	def userEntity() {
		def userEntity = new UserEntity()
		userEntity.setUserId(null)
		userEntity.setUserEmail(EMAIL)
		userEntity.setPassword(PASSWORD)
		userEntity.setName(NAME)
		userEntity.setSurname(SURNAME)
		return userEntity
	}

	def userPutDto() {
		return new UserPutDto(PASSWORD_2, NAME_2, SURNAME_2)
	}

	def userPatchDto() {
		return new UserPatchDto(NAME_2, null, null)
	}

}
