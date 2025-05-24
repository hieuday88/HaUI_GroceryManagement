package util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	private String otp;
	private final String EMAIL = "hauigrocerystore@gmail.com";
	private final String PASSWORD = "fewz hjuh tenj ajwy";
	private final String HOST_NAME = "smtp.gmail.com";
	private final int SSL_PORT = 465;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String generateOtp() {
		return String.valueOf(100000 + (int) (Math.random() * 900000));
	}

	public boolean sendMail(String email, String subject, String content) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", this.HOST_NAME);
		props.put("mail.smtp.socketFactory.port", this.SSL_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", this.SSL_PORT);

		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SendMail.this.EMAIL, SendMail.this.PASSWORD);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(subject);
			message.setContent(content, "text/html; charset=\"utf-8\"");

			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendOtp(String email) {
		String subject = "Mã dùng một lần của bạn";
		setOtp(generateOtp());
		String content = "<html lang=\"vi\"><head><meta charset=\"utf-8\"></head>" + "<body><p>Xin chào " + email
				+ ",<br>"
				+ "<p>Chúng tôi đã nhận yêu cầu mã dùng một lần để dùng cho tài khoản HaUI Grocery Store của bạn.</p><br>"
				+ "<p>Mã dùng một lần của bạn là: <b>" + otp + "</b></p><br>"
				+ "<p>Nếu không yêu cầu mã này thì bạn có thể bỏ qua email này một cách an toàn. Có thể ai đó khác đã nhập địa chỉ email của bạn do nhầm lẫn.</p><br>"
				+ "<p>Xin cảm ơn, <br>HaUI Grocery Store</p>" + "</body></html>";

		return sendMail(email, subject, content);
	}

	public boolean sendNewPassword(String email, String newPassword) {
		String subject = "Mật khẩu mới của bạn";
		String content = "<html lang=\"vi\"><head><meta charset=\"utf-8\"></head>" + "<body><p>Xin chào " + email
				+ ",<br>"
				+ "<p>Chúng tôi đã nhận yêu cầu lấy lại mật khẩu để dùng cho tài khoản HaUI Grocery Store của bạn.</p><br>"
				+ "<p>Mật khẩu mới của bạn là: <b>" + newPassword + "</b></p><br>"
				+ "<p>Nếu không thực hiện yêu cầu này thì bạn có thể bỏ qua email này một cách an toàn. Có thể ai đó khác đã nhập địa chỉ email của bạn do nhầm lẫn.</p><br>"
				+ "<p>Xin cảm ơn, <br>HaUI Grocery Store</p>" + "</body></html>";

		return sendMail(email, subject, content);
	}
}
