package pl.latusikl.notes.services;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

@Component
public class SecretGenerator {

	private static final String KEY_ALGORITHM = "AES";
	private static final int INITIALIZATION_VECTOR_SIZE = 16;
	private static final int KEY_SIZE = 256;
	private final KeyGenerator keyGenerator;

	@SneakyThrows
	public SecretGenerator() {
		this.keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
		keyGenerator.init(KEY_SIZE);
	}

	public IvParameterSpec generateInitializationVector() {
		final byte[] initializationVector = new byte[INITIALIZATION_VECTOR_SIZE];
		final var secureRandom = new SecureRandom();
		secureRandom.nextBytes(initializationVector);
		return new IvParameterSpec(initializationVector);
	}

	@SneakyThrows
	public SecretKey generateKey() {
		return keyGenerator.generateKey();
	}

}
