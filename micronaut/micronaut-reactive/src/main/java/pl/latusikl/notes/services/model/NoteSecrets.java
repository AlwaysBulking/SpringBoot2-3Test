package pl.latusikl.notes.services.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NoteSecrets {
	private final SecretKey key;
	private final IvParameterSpec initVector;
}
