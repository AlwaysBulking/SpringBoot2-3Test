package pl.latusikl.common;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpProtocolProvider {
	public static HttpProtocolBuilder getBaseProtocolConfig() {
		return http.baseUrl(Properties.baseUrl())
				   .disableCaching()
				   .acceptHeader(Headers.Values.APPLICATION_JSON)
				   .acceptEncodingHeader(Constants.ACCEPTED_ENCODINGS)
				   .userAgentHeader(Constants.USER_AGENT);

	}

}
