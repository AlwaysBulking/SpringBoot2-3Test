package pl.latusikl.notes.services;

import io.smallrye.mutiny.Uni;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.latusikl.notes.config.GoogleApiConfig;
import pl.latusikl.notes.services.client.GoogleTokenWebClient;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class GoogleTokenProvider {

	private static final String TOKEN_TEMPLATE = "Bearer %s";
	private static final int TOKEN_SAFE_REFRESH_TIME = 10;
	private static final Duration DEFAULT_TOKEN_CACHE_TIME = Duration.ofSeconds(3600 - TOKEN_SAFE_REFRESH_TIME);

	private final GoogleApiConfig config;
	private final GoogleTokenWebClient tokenWebClient;
	private final GoogleTokenBuilder googleJWTBuilder;
	@Getter
	private final Uni<String> token;

	public GoogleTokenProvider(final GoogleApiConfig config, @RestClient final GoogleTokenWebClient tokenWebClient,
							   final GoogleTokenBuilder googleJWTBuilder) {
		this.config = config;
		this.tokenWebClient = tokenWebClient;
		this.googleJWTBuilder = googleJWTBuilder;
		this.token = tokenMonoWithCache();
	}

	private Uni<String> tokenMonoWithCache() {
		return tokenWebClient.getToken(config.grantType(), googleJWTBuilder.buildTokenForExchange())
							 .memoize()
							 .atLeast(calculateTokenCacheTime())
							 .map(response -> String.format(TOKEN_TEMPLATE, response.getAccessToken()));
	}

	private Duration calculateTokenCacheTime() {
		/*
		Mutiny does not provide possibility to set different memoization time for errors
		and dosen't have operator that would allow calculating caching time based on the Uni value
		to bypass the issue and mimic part of reactor capabilities atomic reference is used to access Uni content
		and pass it to the time calculation function
		*/
		/*
		final var secondsToExpire = tokenResponseDto.getExpiresIn();
		if (nonNull(secondsToExpire) && secondsToExpire > 0) {
			if (secondsToExpire > TOKEN_SAFE_REFRESH_TIME) {
				return Duration.ofSeconds(secondsToExpire - TOKEN_SAFE_REFRESH_TIME);
			}
			return Duration.ofSeconds(secondsToExpire);
		}
		 */
		return DEFAULT_TOKEN_CACHE_TIME;
	}

}
