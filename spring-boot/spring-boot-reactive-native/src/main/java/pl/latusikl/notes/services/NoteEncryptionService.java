package pl.latusikl.notes.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.latusikl.notes.services.model.Note;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteEncryptionService {

	private static final String KEY_TEMPLATE = "%s:%s";
	private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
	private final SecretGenerator secretGenerator;

	public Optional<Note> encrypt(final String value) {
		try {
			final var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			final var key = secretGenerator.generateKey();
			final var initVector = secretGenerator.generateInitializationVector();

			cipher.init(Cipher.ENCRYPT_MODE, key, initVector);
			final byte[] encryptedContent = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
			return Optional.of(buildNote(encryptedContent, key, initVector));
		}
		catch (final InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
			log.warn("Failed to encrypt provided note: {}", value, e);
		}
		return Optional.empty();
	}

	private Note buildNote(final byte[] encryptedContent, final SecretKey key, final IvParameterSpec initVector) {
		final String base64EncodedSecuredNote = base64Encode(encryptedContent);
		final String base64EncodedKey = base64Encode(key.getEncoded());
		final String base64EncodedInitVector = base64Encode(initVector.getIV());
		final String noteKey = String.format(KEY_TEMPLATE, base64EncodedKey, base64EncodedInitVector);
		return new Note(base64EncodedSecuredNote, noteKey);
	}

	private String base64Encode(final byte[] bytes) {
		return Base64.getUrlEncoder()
					 .encodeToString(bytes);
	}
}
