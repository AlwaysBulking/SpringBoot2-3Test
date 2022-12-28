package pl.latusikl.common;

import java.util.UUID;
public class Utils {
	public static String randomUUIDAsString() {
		return UUID.randomUUID()
				   .toString();
	}
}
