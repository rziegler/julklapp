package ch.bbv.julklapp.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WichteliDto {

	private String name;
	private String firstname;
	private byte[] image;

	public WichteliDto() {

	}

	public WichteliDto(String firstname, String name, byte[] image) {
		super();
		this.name = name;
		this.firstname = firstname;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
