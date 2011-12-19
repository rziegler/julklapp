package ch.bbv.julklapp.dto;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CircleDto {
	
	private String name;
	
	@XmlElementWrapper(name = "memberList")
	@XmlElement(name="Member")
	private ArrayList<MemberDto> members;
	
	public CircleDto() {
		members = new ArrayList<MemberDto>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<MemberDto> getMembers() {
		return members;
	}
	
	public void addMember(MemberDto member) {
		members.add(member);
	}
}
