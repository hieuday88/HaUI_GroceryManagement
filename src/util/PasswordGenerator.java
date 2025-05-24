package util;

public class PasswordGenerator {
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int PASSWORD_LENGTH = 12;

	public static String generatePassword() {
		StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (Math.random() * CHARS.length());
			password.append(CHARS.charAt(index));
		}

		return password.toString();
	}
}
