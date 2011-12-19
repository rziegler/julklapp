package ch.bbv.julklapp.clt;
import javax.ws.rs.core.MediaType;


import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.MemberDto;

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
		
//		Circle result = putCircle(resource, "second");
		MemberDto member = new MemberDto();
		member.setName("Kurmann");
		member.setFirstName("Ueli");
		member.setEmail(new Email("ueli.kurmann@bbv.ch"));
		
		System.out.println(member.getEmail().getEmail());
		System.out.println(member.getEmail());
		member.setImage("Ein Bildli...");
		MemberDto resultM = putMember(resource, "second", member);
		
		System.out.println("done.");
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
}
