package ch.bbv.julklapp.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

public class GaeEmailNotifier implements EmailNotifier {

	@Override
	public void sendEmail(CircleDto circle, MemberDto recipient, WichteliDto wichteli, CredentialsDto credentials)
			throws UnsupportedEncodingException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = createMessageBody(circle, recipient, wichteli, credentials);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("zieg" + "ler.r" + " uth" + "@gma" + "il.com", "Julklapp Admin"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getEmail(), recipient.getFirstName()
				+ " " + recipient.getName()));
		msg.setSubject("Your Julklapp Wichteli has been assigned.");
		msg.setText(msgBody);
		Transport.send(msg);
	}

	private String createMessageBody(CircleDto circle, MemberDto recipient, WichteliDto wichteli,
			CredentialsDto credentials) {
		StringBuilder builder = new StringBuilder();

		builder.append("Hi " + recipient.getFirstName() + ",\n\n");
		builder.append("Your Julklapp Wichteli has been assigned for circle '");
		builder.append(circle.getName());
		builder.append("'.\n\n");
		builder.append("To view your Wichteli follow these steps:\n\n");
		builder.append("1. Open your Julklapp application on your Android device.\n");
		builder.append("2. Enter your username: " + credentials.getUsername() + "\n");
		builder.append("3. Enter your password: " + credentials.getPassword() + "\n");
		builder.append("4. Press 'Show Wichteli'\n\n\n");
		builder.append("Happy gift making,\n");
		builder.append("Your Julklapp team.\n");
		return builder.toString();
	}
}
