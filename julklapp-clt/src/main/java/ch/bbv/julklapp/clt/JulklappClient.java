package ch.bbv.julklapp.clt;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

@SuppressWarnings("unused")
public class JulklappClient {

	private static final String CIRCLE_THIRD = "third";
	private static final String CIRCLE_SECOND = "second";
	private static final String CIRCLE_FIRST = "first";

	private static final List<MemberDto> members = new ArrayList<MemberDto>();
	private final String uri;

	static {
		members.add(createMember("Ueli", "Kurmann", "ueli.kurmann@gmail.com", "bild.jpg"));
		members.add(createMember("Ruth", "Ziegler", "ziegler.ruth@gmail.com", "bild.jpg"));
		members.add(createMember("Lukas", "Weibel", "ueli.kurmann@bbv.ch", "bild.jpg"));
		members.add(createMember("Gabi", "Gwinner", "ruth.ziegler@bbv.ch", "bild.jpg"));
	}

	private static MemberDto createMember(String firstName, String name, String email, String image) {
		MemberDto member = new MemberDto();
		member.setName(name);
		member.setFirstName(firstName);
		member.setEmail(email);
		member.setImage(image);
		return member;
	}

	public JulklappClient(String uri) {
		this.uri = uri;
	}

	public void run() {
		Client client = Client.create();
		WebResource resource = client.resource(uri);

		putCircle(resource, CIRCLE_FIRST);
		putCircle(resource, CIRCLE_SECOND);
		putCircle(resource, CIRCLE_THIRD);

		initMembers(resource, CIRCLE_FIRST, 2);
		initMembers(resource, CIRCLE_SECOND, 3);
		initMembers(resource, CIRCLE_THIRD, 4);

		// WichteliDto w1 = queryWichteli(resource, CIRCLE_SECOND, "Ueli",
		// "ueli.kurmann@bbv.ch", "kdbiwrmr");
		// WichteliDto w2 = queryWichteli(resource, CIRCLE_SECOND, "Ruth",
		// "ruth.ziegler@bbv.ch", "fxgwnpon");
		// WichteliDto w3 = queryWichteli(resource, CIRCLE_SECOND, "Gabi",
		// "gabi.gwinner@bbv.ch", "whqjpcbi");
		// WichteliDto w4 = queryWichteli(resource, CIRCLE_SECOND, "Lukas",
		// "lukas.weibel@bbv.ch", "nthwyjaj");

		System.out.println("Done [" + resource.getURI().toString() + "]");
	}

	private void initMembers(WebResource resource, String circle, int numOfMembers) {
		for (int i = 0; i < numOfMembers; i++) {
			putMember(resource, circle, members.get(i % members.size()));
		}
	}

	private CircleDto putCircle(WebResource resource, String name) {
		WebResource resourceWithPath = resource.path("/circles/" + name);

		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		CircleDto circle = new CircleDto();
		circle.setName(name);
		CircleDto result = builder.put(CircleDto.class, circle);
		return result;
	}

	private MemberDto putMember(WebResource resource, String circle, MemberDto member) {
		WebResource resourceWithPath = resource.path("/circles/" + circle + "/" + member.getFirstName());

		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		MemberDto result = builder.put(MemberDto.class, member);
		return result;
	}

	private WichteliDto queryWichteli(WebResource resource, String circle, String name, String email, String password) {
		WebResource resourceWithPath = resource.path("/circles/" + circle + "/" + name + "/wichteli");

		CredentialsDto cred = new CredentialsDto();
		cred.setUsername(email);
		cred.setPassword(password);

		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		WichteliDto result = builder.post(WichteliDto.class, cred);

		System.out.println("Wichteli of " + name + " is " + result.getFirstname());
		return result;
	}
}
