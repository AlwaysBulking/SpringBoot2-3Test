package pl.latusikl.database;

import pl.latusikl.common.Utils;

public class UserBodyUtils {

	public static final String USER_EMAIL_PATH = "email";
	public static final String USER_NAME_PATH = "name";
	public static final String USER_SURNAME_PATH = "surname";
	private static final String EMAIL_TEMPLATE = "%s@email.com";
	private static final String PASSWORD_TEMPLATE = "P@sswor0d-%s";
	private static final String NAME_TEMPLATE = "John-%s";
	private static final String SURNAME_TEMPLATE = "Doe-%s";

	private static final String POST_BODY_TEMPLATE = """
													 {
													 "email": "%s",
													 "password": "%s",
													 "name": "%s",
													 "surname": "%s"
													 }
													 """;
	private static final String PUT_BODY_TEMPLATE = """
													{
													"name": "%s",
													"surname": "%s",
													"password": "%s"
													}
													""";

	private static final String PATCH_BODY_TEMPLATE = """
													  {
													  "name": "%s"
													  }
													  """;

	public static String createUserBody(final String name, final String surname, final String password, final String email) {
		return String.format(POST_BODY_TEMPLATE, email, password, name, surname);
	}

	public static String patchUserBody(final String name) {
		return String.format(PATCH_BODY_TEMPLATE, name);
	}

	public static String putUserBody(final String name, final String surname, final String password) {
		return String.format(PUT_BODY_TEMPLATE, name, surname, password);
	}

	public static String randomizedEmail() {
		return randomizedTemplate(EMAIL_TEMPLATE);
	}

	public static String randomizedName() {
		return randomizedTemplate(NAME_TEMPLATE);
	}

	public static String randomizedSurname() {
		return randomizedTemplate(SURNAME_TEMPLATE);
	}

	public static String randomizedPassword() {
		return randomizedTemplate(PASSWORD_TEMPLATE);
	}

	private static String randomizedTemplate(final String template) {
		return String.format(template, Utils.randomUUIDAsString());
	}
}
