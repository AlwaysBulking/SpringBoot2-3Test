package pl.latusikl.common;

import java.time.Duration;

public class Constants {

	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0";
	public static final String ACCEPTED_ENCODINGS = "gzip, deflate, br";
	public static final double SUCCESS_PERCENTAGE = 95;
	public static final Duration DEFAULT_MIN_PAUSE_AFTER_REQUEST = Duration.ofSeconds(5);
	public static final Duration DEFAULT_MAX_PAUSE_AFTER_REQUEST = Duration.ofSeconds(10);
	public static final Duration SSE_PAUSE = Duration.ofSeconds(1);

	public static final Duration SSE_WAIT_TIME = Duration.ofSeconds(10);
}
