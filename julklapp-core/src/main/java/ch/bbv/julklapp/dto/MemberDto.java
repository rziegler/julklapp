package ch.bbv.julklapp.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.bbv.julklapp.dto.adapter.EmailAdapter;

import com.google.appengine.api.datastore.Email;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberDto {
	
	private String name;
	
	private String firstName;
	
	@XmlJavaTypeAdapter(EmailAdapter.class)
	private Email email;
	
	private String image;

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
}
