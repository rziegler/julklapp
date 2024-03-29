package ch.bbv.julklapp.core.dao;

import java.util.ArrayList;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.Key;

@Entity
@XmlRootElement
public class Circle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	@Basic
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@XmlElementWrapper(name = "memberList")
	@XmlElement(name="Member")
	private ArrayList<Member> members;
	
	public Circle() {
		members = new ArrayList<Member>();
	}
	
	public Key getKey() {
		return key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Member> getMembers() {
		return members;
	}
	
	public void addMember(Member member) {
		members.add(member);
	}
}
