package ch.bbv.julklapp.clt;
import javax.ws.rs.core.MediaType;


import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

import com.google.appengine.api.datastore.Email;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;


public class JulklappClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = Client.create();
		WebResource resource = client.resource("http://localhost:8888/");
		
		//CircleDto result = putCircle(resource, "second");
		//initMembers(resource);
		WichteliDto wichteli = queryWichteli(resource, "second", "Ueli");
		
		
		System.out.println("done.");
	}

	private static void initMembers(WebResource resource) {
		MemberDto m1 = new MemberDto();
		m1.setName("Kurmann");
		m1.setFirstName("Ueli");
		m1.setEmail("ueli.kurmann@bbv.ch");
		m1.setImage("Ein Bildli...");
		
		MemberDto m2 = new MemberDto();
		m2.setName("Ziegler");
		m2.setFirstName("Ruth");
		m2.setEmail("ruth.ziegler@bbv.ch");
		m2.setImage("Ein Bildli...");
		
		MemberDto m3 = new MemberDto();
		m3.setName("Gwinner");
		m3.setFirstName("Gabi");
		m3.setEmail("gabi.gwinner@bbv.ch");
		m3.setImage("Ein Bildli...");
		
		MemberDto m4 = new MemberDto();
		m4.setName("Weibel");
		m4.setFirstName("Lukas");
		m4.setEmail("lukas.weibel@bbv.ch");
		m4.setImage("Ein Bildli...");
		
		putMember(resource, "second", m1);
		putMember(resource, "second", m2);
		putMember(resource, "second", m3);
		putMember(resource, "second", m4);
	}

	private static CircleDto putCircle(WebResource resource, String name) {
		WebResource resourceWithPath = resource.path("/circles/" + name);
		
		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		CircleDto circle = new CircleDto();
		circle.setName(name);
		CircleDto result = builder.put(CircleDto.class, circle);
		return result;
	}
	
	private static MemberDto putMember(WebResource resource, String circle, MemberDto member) {
		WebResource resourceWithPath = resource.path("/circles/" + circle+"/" + member.getFirstName());
		
		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		MemberDto result = builder.put(MemberDto.class, member);
		return result;
	}
	

	private static WichteliDto queryWichteli(WebResource resource, String circle, String name) {
		WebResource resourceWithPath = resource.path("/circles/" + circle+"/" + name + "/wichteli");
		
		CredentialsDto cred = new CredentialsDto();
		cred.setUsername("ueli.kurmann@bbv.ch");
		cred.setPassword("test123");
		
		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		WichteliDto result = builder.post(WichteliDto.class, cred);
		
		System.out.println("Wichteli of " + name + " is " + result.getFirstname());
		return result;
	}
	
	
}
