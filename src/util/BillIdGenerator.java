package util;

public class BillIdGenerator {
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int BILLID_LENGTH = 8;

	public static String generateBillId() {
		StringBuilder billId = new StringBuilder(BILLID_LENGTH);

		for (int i = 0; i < BILLID_LENGTH; i++) {
			int index = (int) (Math.random() * CHARS.length());
			billId.append(CHARS.charAt(index));
		}

		return billId.toString();
	}
}
