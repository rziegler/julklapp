package ch.bbv.julklapp.core.dao.transformer;

import ch.bbv.julklapp.core.dao.Member;
import ch.bbv.julklapp.dto.MemberDto;

import com.google.appengine.api.datastore.Email;

public class MemberTransformer {

	public Member marshal(MemberDto memberDto) {
		Member member = new Member();
		member.setName(memberDto.getName());
		member.setFirstName(memberDto.getFirstName());
		member.setEmail(new Email(memberDto.getEmail()));
		// member.setImage(memberDto.getImage());
		return member;
	}

	public MemberDto unmarshal(Member member) {
		MemberDto memberDto = new MemberDto();
		memberDto.setName(member.getName());
		memberDto.setFirstName(member.getFirstName());
		memberDto.setEmail(member.getEmail().getEmail());
		// memberDto.setImage(member.getImage());
		return memberDto;
	}
}
