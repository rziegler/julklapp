package ch.bbv.julklapp.email;

import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

public interface EmailNotifier {

	void sendEmail(MemberDto recipient, WichteliDto wichteli);
}
