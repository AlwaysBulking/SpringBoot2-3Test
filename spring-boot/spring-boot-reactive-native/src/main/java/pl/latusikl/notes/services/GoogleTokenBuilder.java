package pl.latusikl.notes.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.latusikl.notes.config.GoogleApiConfig;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class GoogleTokenBuilder {

	private static final String KEY_BEGINNING = "-----BEGIN PRIVATE KEY-----";
	private static final String KEY_END = "-----END PRIVATE KEY-----";
	private static final String END_LN = "\\n";
	private static final String EMPTY_STR = "";
	private static final String SCOPE_CLAIM = "scope";
	private static final long TOKEN_VALIDITY = 3600 * 1000L;

	private final GoogleApiConfig apiConfig;
	private final Algorithm algorithm;

	public GoogleTokenBuilder(final GoogleApiConfig apiConfig) {
		this.apiConfig = apiConfig;
		this.algorithm = Algorithm.RSA256(null, toRSAPrivateKey(apiConfig.getPrivateKey()));
	}

	@SneakyThrows
	private static RSAPrivateKey toRSAPrivateKey(final String privateKey) {
		final var base64EncodedKeyContent = privateKey.replace(KEY_BEGINNING, EMPTY_STR)
													  .replace(KEY_END, EMPTY_STR)
													  .replace(END_LN, EMPTY_STR);

		final byte[] keyBytes = Base64.getDecoder()
									  .decode(base64EncodedKeyContent);

		final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		return (RSAPrivateKey) KeyFactory.getInstance("RSA")
										 .generatePrivate(keySpec);
	}

	public String buildTokenForExchange() {
		final var currentTime = System.currentTimeMillis();
		final var currDate = new Date(currentTime);
		final var expiresDate = new Date(currentTime + TOKEN_VALIDITY);

		return JWT.create()
				  .withIssuer(apiConfig.getUserEmail())
				  .withClaim(SCOPE_CLAIM, apiConfig.getScopes())
				  .withAudience(apiConfig.getAuthUrl())
				  .withIssuedAt(currDate)
				  .withExpiresAt(expiresDate)
				  .withSubject(apiConfig.getUserEmail())
				  .sign(algorithm);
	}

}
