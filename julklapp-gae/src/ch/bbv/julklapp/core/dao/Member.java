package ch.bbv.julklapp.core.dao;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

@Entity
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	@Basic
	private String name;
	
	@Basic
	private String firstName;
	
	@Basic
	private String password;
	
	@Basic
	private Email email;
	
	@Basic
	private String image;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	private Member wichteli;
	
	public Key getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public Member getWichteli() {
		return wichteli;
	}
	
	public void setWichteli(Member wichteli) {
		this.wichteli = wichteli;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
