package pl.latusikl.notes.services;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import jakarta.inject.Singleton;
import lombok.Getter;
import pl.latusikl.notes.config.GoogleApiConfig;
import pl.latusikl.notes.dto.TokenResponseDto;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static java.util.Objects.nonNull;

@Singleton
public class GoogleTokenProvider {

	private static final String GRANT_TYPE = "grant_type";
	private static final String ASSERTION = "assertion";
	private static final String TOKEN_TEMPLATE = "Bearer %s";
	private static final int TOKEN_SAFE_REFRESH_TIME = 10;
	private static final Duration DEFAULT_TOKEN_CACHE_TIME = Duration.ofSeconds(3600 - TOKEN_SAFE_REFRESH_TIME);

	private final GoogleApiConfig config;
	private final ReactorHttpClient httpClient;
	private final GoogleTokenBuilder googleJWTBuilder;
	@Getter
	private final Mono<String> token;

	public GoogleTokenProvider(final GoogleApiConfig config, final ReactorHttpClient httpClient,
							   final GoogleTokenBuilder googleJWTBuilder) {
		this.config = config;
		this.httpClient = httpClient;
		this.googleJWTBuilder = googleJWTBuilder;
		this.token = tokenMonoWithCache();
	}

	private Mono<String> tokenMonoWithCache() {
		final var requestBody = Map.of(GRANT_TYPE, config.getGrantType(), ASSERTION, googleJWTBuilder.buildTokenForExchange());
		return httpClient.retrieve(HttpRequest.POST(config.getAuthUrl(), requestBody)
											  .contentType(MediaType.APPLICATION_FORM_URLENCODED), TokenResponseDto.class)
						 .next()
						 .cache(this::calculateTokenCacheTime, throwable -> Duration.ZERO, () -> Duration.ZERO)
						 .map(tokenResponseDto -> String.format(TOKEN_TEMPLATE, tokenResponseDto.getAccessToken()));
	}

	private Duration calculateTokenCacheTime(final TokenResponseDto tokenResponseDto) {
		final var secondsToExpire = tokenResponseDto.getExpiresIn();
		if (nonNull(secondsToExpire) && secondsToExpire > 0) {
			if (secondsToExpire > TOKEN_SAFE_REFRESH_TIME) {
				return Duration.ofSeconds(secondsToExpire - TOKEN_SAFE_REFRESH_TIME);
			}
			return Duration.ofSeconds(secondsToExpire);
		}
		return DEFAULT_TOKEN_CACHE_TIME;
	}

}
