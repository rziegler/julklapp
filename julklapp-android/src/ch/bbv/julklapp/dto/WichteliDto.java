package ch.bbv.julklapp.dto;


public class WichteliDto {

	private String name;
	private String firstname;
	
	public WichteliDto() {
		
	}

	public WichteliDto(String firstname, String name) {
		super();
		this.name = name;
		this.firstname = firstname;
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
}
