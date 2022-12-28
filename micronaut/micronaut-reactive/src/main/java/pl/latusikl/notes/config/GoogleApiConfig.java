package pl.latusikl.notes.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ConfigurationProperties("google.api")
public class GoogleApiConfig {

	private static final String SCOPE_SPLITTER = ",";

	@ToString.Exclude
	private String userEmail;

	@ToString.Exclude
	private String privateKey;

	@Getter(AccessLevel.NONE)
	@ToString.Exclude
	private String scopes;

	private String storageApiUrl;

	private String authUrl;

	private String grantType;

	private String bucketName;

	public String getScopes() {
		return scopes.replace(SCOPE_SPLITTER, " ");
	}

}
