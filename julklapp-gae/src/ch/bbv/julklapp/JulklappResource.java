package ch.bbv.julklapp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/test")
public class JulklappResource {

	@GET
	@Produces("text/plain")
	public String getClichedMessage() {
		return "Hello World";
	}

	@GET
	@Produces("text/plain")
	@Path("/thursday/")
	public String getThursday() {
		return "Hello Thursday";
	}

	@GET
	@Produces("text/plain")
	@Path("/mail/")
	public String sendMail() {
		sendEmail();
		return "Mail sent.";
	}

	@GET
	@Produces("text/plain")
	@Path("/hello/{name}")
	public String sayHello(@PathParam("name") String name) {
		return "Hello " + name;
	}

	private void sendEmail() {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Message body";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("ziegler.ruth@gmail.com", "Julklapp Admin"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress("ziegler.ruth@gmail.com", "Ruth Ziegler"));
			msg.setSubject("Your Julklapp Wichteli has been assigned.");
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
