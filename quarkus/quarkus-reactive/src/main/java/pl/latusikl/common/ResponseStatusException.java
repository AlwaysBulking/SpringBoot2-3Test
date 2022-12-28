package pl.latusikl.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ResponseStatusException extends RuntimeException {

	@Getter
	private final int httpStatus;

	public ResponseStatusException(final String message, final int httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
