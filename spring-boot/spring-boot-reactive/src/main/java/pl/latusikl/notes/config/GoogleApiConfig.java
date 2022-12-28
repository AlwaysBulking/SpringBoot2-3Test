package pl.latusikl.notes.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("google.api")
@Getter
@Setter
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
