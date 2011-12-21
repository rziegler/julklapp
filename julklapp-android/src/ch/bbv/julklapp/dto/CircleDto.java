package ch.bbv.julklapp.dto;


public class CircleDto {

	private String name;

	private MembersDto memberList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MembersDto getMemberList() {
		return memberList;
	}

	public void setMemberList(MembersDto data) {
		this.memberList = data;
	}

	@Override
	public String toString() {
		return name;
	}
}
