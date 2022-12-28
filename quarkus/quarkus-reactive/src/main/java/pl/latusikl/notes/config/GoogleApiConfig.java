package pl.latusikl.notes.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "google.api")
public interface GoogleApiConfig {

	String userEmail();

	String privateKey();

	String scopes();

	String grantType();

	String bucketName();

	String authUrl();

	default String getFormattedScopes() {
		return this.scopes()
				   .replace(",", " ");
	}

}
