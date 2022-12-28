package pl.latusikl.notes.services;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import pl.latusikl.notes.services.model.NoteSecrets;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Singleton
public class NoteDecryptionService {

	private static final String KEY_SPLITTER = ":";
	private static final int SPLIT_SIZE = 2;
	private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String KEY_ALGORITHM = "AES";

	public Optional<String> decrypt(final String encodedSecuredContent, final String encodedKey) {
		final Optional<NoteSecrets> optionalNoteSecrets = parseKey(encodedKey);
		if (optionalNoteSecrets.isPresent()) {
			try {
				final var noteSecrets = optionalNoteSecrets.get();
				final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, noteSecrets.getKey(), noteSecrets.getInitVector());
				final byte[] noteBytes = cipher.doFinal(base64Decode(encodedSecuredContent));
				return Optional.of(new String(noteBytes, StandardCharsets.UTF_8));
			}
			catch (final InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
				log.warn("Failed to encrypt provided note: {}", encodedSecuredContent, e);
			}
			return Optional.empty();
		}
		return Optional.empty();
	}

	private Optional<NoteSecrets> parseKey(final String encodedKey) {
		final String[] keySplit = encodedKey.split(KEY_SPLITTER);
		if (keySplit.length != SPLIT_SIZE) {
			return Optional.empty();
		}
		final var key = new SecretKeySpec(base64Decode(keySplit[0]), KEY_ALGORITHM);
		final var initVector = new IvParameterSpec(base64Decode(keySplit[1]));
		return Optional.of(new NoteSecrets(key, initVector));
	}

	private byte[] base64Decode(final String base64String) {
		return Base64.getUrlDecoder()
					 .decode(base64String);
	}
}
