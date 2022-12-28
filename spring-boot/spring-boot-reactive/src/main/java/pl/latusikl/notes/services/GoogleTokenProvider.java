package pl.latusikl.notes.services;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pl.latusikl.notes.config.GoogleApiConfig;
import pl.latusikl.notes.dto.TokenResponseDto;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static java.util.Objects.nonNull;

@Component
public class GoogleTokenProvider {

	private static final String GRANT_TYPE = "grant_type";
	private static final String ASSERTION = "assertion";
	private static final String TOKEN_TEMPLATE = "Bearer %s";
	private static final int TOKEN_SAFE_REFRESH_TIME = 10;
	private static final Duration DEFAULT_TOKEN_CACHE_TIME = Duration.ofSeconds(3600 - TOKEN_SAFE_REFRESH_TIME);

	private final GoogleApiConfig config;
	private final WebClient googleWebClient;
	private final GoogleTokenBuilder googleJWTBuilder;
	private final WebClientErrorHandler errorHandler;
	@Getter
	private final Mono<String> token;

	public GoogleTokenProvider(final GoogleApiConfig config, final WebClient googleWebClient,
							   final GoogleTokenBuilder googleJWTBuilder, final WebClientErrorHandler errorHandler) {
		this.config = config;
		this.googleWebClient = googleWebClient;
		this.googleJWTBuilder = googleJWTBuilder;
		this.errorHandler = errorHandler;
		this.token = tokenMonoWithCache();
	}

	private Mono<String> tokenMonoWithCache() {
		final var requestBody = BodyInserters.fromFormData(GRANT_TYPE, config.getGrantType())
											 .with(ASSERTION, googleJWTBuilder.buildTokenForExchange());
		return googleWebClient.post()
							  .uri(config.getAuthUrl())
							  .body(requestBody)
							  .retrieve()
							  .onStatus(HttpStatus::isError, this::handleTokenExchangeError)
							  .bodyToMono(TokenResponseDto.class)
							  .cache(this::calculateTokenCacheTime, throwable -> Duration.ZERO, () -> Duration.ZERO)
							  .map(tokenResponseDto -> String.format(TOKEN_TEMPLATE, tokenResponseDto.getAccessToken()));

	}

	private Mono<Throwable> handleTokenExchangeError(final ClientResponse clientResponse) {
		return errorHandler.handleErrorStatus(clientResponse, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during processing.",
											  "Error occurred when trying to exchange token for Google API.");
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
