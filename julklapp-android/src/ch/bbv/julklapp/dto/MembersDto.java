package ch.bbv.julklapp.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonSetter;

public class MembersDto {
	
	private List<MemberDto> member;
	
	@JsonGetter(value="Member")
	public List<MemberDto> getMember() {
		return member;
	}
	@JsonSetter(value="Member")
	public void setMember(List<MemberDto> members) {
		member = members;
	}

}
