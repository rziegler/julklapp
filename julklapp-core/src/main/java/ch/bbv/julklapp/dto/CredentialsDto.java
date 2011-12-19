package ch.bbv.julklapp.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CredentialsDto {
	
	private String username;
	private String password;
	
	public CredentialsDto() {
		
	}
		
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
