import javax.ws.rs.core.MediaType;

import ch.bbv.julklapp.core.dao.Circle;
import ch.bbv.julklapp.core.dao.Member;

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
		
		Circle result = putCircle(resource, "second");
		Member member = new Member();
		member.setName("Kurmann");
		member.setFirstName("Ueli");
		member.setEmail("ueli.kurmann@bbv.ch");
		member.setImage("Ein Bildli...");
		Member resultM = putMember(resource, "second", member);
		
		System.out.println("done.");
	}

	private static Circle putCircle(WebResource resource, String name) {
		WebResource resourceWithPath = resource.path("/circles/" + name);
		
		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		Circle circle = new Circle();
		circle.setName(name);
		Circle result = builder.put(Circle.class, circle);
		return result;
	}
	
	private static Member putMember(WebResource resource, String circle, Member member) {
		WebResource resourceWithPath = resource.path("/circles/" + circle+"/" + member.getFirstName());
		
		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		Member result = builder.put(Member.class, member);
		return result;
	}
}