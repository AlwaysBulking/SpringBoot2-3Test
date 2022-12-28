package pl.latusikl.common;

import java.util.Objects;

public class Properties {

	private static final String BASE_URL_PROP = "baseUrl";
	private static final String STRESS_SINGLE_GREETING_USERS = "sgUsr";
	private static final String STRESS_SINGLE_GREETING_TIME = "sgTime";
	private static final String DEFAULT_SINGLE_GREETING_USERS = "20000";
	private static final String DEFAULT_SINGLE_GREETING_TIME = "10";

	private static final String STRESS_SINGLE_GREETING_DELAY_USERS = "sgdUsr";
	private static final String STRESS_SINGLE_GREETING_DELAY_TIME = "sgdTime";
	private static final String DEFAULT_SINGLE_GREETING_DELAY_USERS = "8000";
	private static final String DEFAULT_SINGLE_GREETING_DELAY_TIME = "10";

	private static final String STRESS_SSE_GREETING_USERS = "sseUsr";
	private static final String STRESS_SSE_GREETING_TIME = "sseTime";
	private static final String DEFAULT_SSE_GREETING_USERS = "15000";
	private static final String DEFAULT_SSE_GREETING_TIME = "10";

	private static final String STRESS_MEDIUM_NUM_SET_SORT_USERS = "sortmUsr";
	private static final String STRESS_MEDIUM_NUM_SET_SORT_TIME = "sortmTime";
	private static final String DEFAULT_STRESS_MEDIUM_NUM_SET_SORT_USERS = "5000";
	private static final String DEFAULT_STRESS_MEDIUM_NUM_SET_SORT_TIME = "10";

	private static final String STRESS_CREATE_FETCH_DELETE_USERS = "dbcrdUsr";
	private static final String STRESS_CREATE_FETCH_DELETE_TIME = "dbcrdTime";
	private static final String DEFAULT_STRESS_CREATE_FETCH_DELETE_USERS = "20000";
	private static final String DEFAULT_STRESS_CREATE_FETCH_DELETE_TIME = "10";

	public static String baseUrl() {
		return System.getProperty(BASE_URL_PROP);
	}

	public static int singleGreetingUsers() {
		return Integer.valueOf(
				Objects.requireNonNullElse(System.getProperty(STRESS_SINGLE_GREETING_USERS), DEFAULT_SINGLE_GREETING_USERS));
	}

	public static int singleGreetingTime() {
		return Integer.valueOf(
				Objects.requireNonNullElse(System.getProperty(STRESS_SINGLE_GREETING_TIME), DEFAULT_SINGLE_GREETING_TIME));
	}

	public static int singleGreetingDelayUsers() {
		return Integer.valueOf(
				Objects.requireNonNullElse(System.getProperty(STRESS_SINGLE_GREETING_DELAY_USERS), DEFAULT_SINGLE_GREETING_DELAY_USERS));
	}

	public static int singleGreetingDelayTime() {
		return Integer.valueOf(
				Objects.requireNonNullElse(System.getProperty(STRESS_SINGLE_GREETING_DELAY_TIME), DEFAULT_SINGLE_GREETING_DELAY_TIME));
	}

	public static int sseUsers() {
		return Integer.valueOf(Objects.requireNonNullElse(System.getProperty(STRESS_SSE_GREETING_USERS), DEFAULT_SSE_GREETING_USERS));
	}

	public static long sseTime() {
		return Integer.valueOf(Objects.requireNonNullElse(System.getProperty(STRESS_SSE_GREETING_TIME), DEFAULT_SSE_GREETING_TIME));
	}

	public static int mediumSortUsers() {
		return Integer.valueOf(Objects.requireNonNullElse(System.getProperty(STRESS_MEDIUM_NUM_SET_SORT_USERS), DEFAULT_STRESS_MEDIUM_NUM_SET_SORT_USERS));
	}

	public static long mediumSortTime() {
		return Integer.valueOf(Objects.requireNonNullElse(System.getProperty(STRESS_MEDIUM_NUM_SET_SORT_TIME), DEFAULT_STRESS_MEDIUM_NUM_SET_SORT_TIME));
	}

	public static int createReadDeleteUsers() {
		return Integer.valueOf(Objects.requireNonNullElse(System.getProperty(STRESS_CREATE_FETCH_DELETE_USERS), DEFAULT_STRESS_CREATE_FETCH_DELETE_USERS));
	}

	public static long createReadDeleteTime() {
		return Integer.valueOf(Objects.requireNonNullElse(System.getProperty(STRESS_CREATE_FETCH_DELETE_TIME), DEFAULT_STRESS_CREATE_FETCH_DELETE_TIME));
	}

}
