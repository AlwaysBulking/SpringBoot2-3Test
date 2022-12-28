package pl.latusikl.notes.services

import com.auth0.jwt.JWT
import pl.latusikl.notes.config.GoogleApiConfig
import spock.lang.Specification

class GoogleTokenBuilderSpec extends Specification {

	def samplePrivateKey = "-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDB2IG+VLsRZ2Ym\\n79UN35iyFViUKyBdpERdkDekFAyeA3mEnJyWWX2uw5fLk8oimmz17qNbX6Hfebrj\\nv2hP/VXEHM/696c6ZjRHluHJ4eRot7c1CSCvSrBxJj4HGew9cLUbiDqF0djhb5aA\\nYkQtTrTyiDzQKYbiaPDyGRVuI7CgNhvyU1fiZ/HPQ3+ezUN2fRykB+br0RDJ4S8b\\nnWWWJNS1mfmzEq5xQxl1gwQMi/AsLzCEY2K66WDJVVcw4T4SkiQJZaoCOqv6ye+7\\nrlhZ7m2jnUNDDA1nQKstF61DzvngmHNtkYFAyZ9UUhcDatsKtT2vuCSASUTOmts6\\nX7oc9GC5AgMBAAECggEAF2ckeN5hbF87p8FGS2O+m2d014SiZnPN1i7ybMKH6G7a\\nda5zwDojMJ2IHsmjuoxC0elC+yXfVOvtKpBiD2AyQdhJqs7J94S91JPMhDNbwGHH\\nwmUs90TQ6wQ3MmPIj/1ivCV1DVFxufvfI3ZZrU+mjG5yyx3sXTrWth/dpLu12A2i\\nSVcmWkEdDb+EH/BOODr99ec8m70p/YywsNY6v8fakLOfCbkmCFRSQZ6srOqK6+t5\\nb8QQVjmK1wOa2VZ03psrBMHqbHr2MclVDJugq6CTD/prK7D+2JvUA+HzkT+vuorv\\n7fnTcSAtTq2HrKBohtEtjrZDBtiNFTkcjLm6zP6dQQKBgQD0lO4sItr4Nl5iW/eV\\nFO5j6cAsqpu75mW7yaBceR1Rv72PyFj73pGRIYf6ihfeAHKxr+AmJ+u8tyLxe3Hu\\nAc119HYD5Yb4jSESJp5jmPplzTaIYXVJjhEs44/4GpOOnbcZ5fc4xqaCGd6+ioqT\\naX6gbjrS0PNK7OiZCyWI/yYF4QKBgQDK5TcK8YgxUy8hVtkz5oVr5XF9QBdjR5UZ\\nobKA2bYvN08UbqV70CXzZG1eqiuJXAFNgWKDsGGK6c/ItV5JkXd8O795g6adoyx7\\nhDtoY9Pz45YmPUDH22VsxsLD2g8/SJXmJZl6FjiuTeSd5IKTODs0Y5sDXcyK/ATR\\nw7w90xwF2QKBgG2A2HnOSTdkMDHddyW6L1lwaGgXIo6oAthm13NPjn7xV8+uJZju\\n90GBP74+YZ01miRP2A19RiBB5lUzrbif+ftbrhLkit9xYGMeA0AYV9OIY2gAKmD6\\nSFpOsECmtXXeywG/YkZ7OkV5i67HnG+PhfASKigFAUiEdUdAJFgH1GBBAoGBALFd\\n3VdQu9k6ucmwTWIkk+A1jJeMjqV4bywgAZbIO45dEGFxNHLfDD4QPdfydOq4Jl9l\\nywQKDctNCBqSCmHPq59L79+/KclGWt2DRBfGpQPgoWK+S0IrVifYPWzO1GNjBb9S\\ngS+L+3LMnx00JL2j+4WZyWoQ9n1NQL1LGeUGBspJAoGBAPP+P/U99WuFYrtOLWai\\nOMBvcacZLVkIsFSMm4TO7K/hoahZcdkmP2Z5shHmPwBSPHgWsllL0drx+jFMSjrN\\nVrK3Agmv4jujkYNQeL9oBL0HC3XTNqeOS6wXf/0SHRpzDWDGYqL136/gD/JV8mUw\\nhLjPdfmZDt7Xt9e7QP79Kcpc\\n-----END PRIVATE KEY-----\\n"

	GoogleApiConfig config = Mock()

	def setup() {
		config.getPrivateKey() >> samplePrivateKey
	}

	def "Should create Algorithm object when constructor invoked"() {
		when:
		def subject = new GoogleTokenBuilder(config)
		def algorithm = subject.algorithm
		then:
		algorithm != null
		algorithm.getName() == "RS256"
	}

	def "Should generate token"() {
		given:
		def subject = new GoogleTokenBuilder(config)
		and:
		def userEmail = "email@email.pl"
		config.getUserEmail() >> userEmail
		and:
		def scope = "scope1"
		config.getScopes() >> scope
		and:
		def authUrl = "https://auth.com"
		config.getAuthUrl() >> authUrl

		when:
		def token = subject.buildTokenForExchange()

		then:
		token != null
		def decodedToken = JWT.decode(token)
		decodedToken.getHeaderClaim("typ").asString() == "JWT"
		decodedToken.getHeaderClaim("alg").asString() == "RS256"
		decodedToken.getIssuer() == userEmail
		decodedToken.getAudience().get(0) == authUrl
		decodedToken.getSubject() == userEmail
		decodedToken.getClaim("scope").asString() == scope
	}

}
