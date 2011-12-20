package ch.bbv.julklapp.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

public interface EmailNotifier {

	void sendEmail(CircleDto circle, MemberDto recipient, WichteliDto wichteli, CredentialsDto credentials)
			throws UnsupportedEncodingException, MessagingException;
}
