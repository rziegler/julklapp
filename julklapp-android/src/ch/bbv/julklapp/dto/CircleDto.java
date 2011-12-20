package ch.bbv.julklapp.dto;

import java.util.ArrayList;

public class CircleDto {
	
	private String name;
	
	private ArrayList<MemberDto> memberList;
	
	public CircleDto() {
		memberList = new ArrayList<MemberDto>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<MemberDto> getMemberList() {
		return memberList;
	}
	
	public void setMemberList( ArrayList<MemberDto> data){
		this.memberList = data;
	}
	
	public void addMember(MemberDto member) {
		memberList.add(member);
	}
}
