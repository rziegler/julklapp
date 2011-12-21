package ch.bbv.julklapp.clt;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
	private static MemberDto ueli;
	private static MemberDto ruth;
	private static MemberDto lukas;
	private static MemberDto gabi;
	private final String uri;
	private final Client client;
	private final WebResource resource;

	static {
		try {
			members.add(createMember("Ueli", "Kurmann", "ueli.kurmann@gmail.com", "ueli.gif"));
			members.add(createMember("Ruth", "Ziegler", "ziegler.ruth@gmail.com", "ruth.gif"));
			members.add(createMember("Lukas", "Weibel", "ueli.kurmann@bbv.ch", "lukas.gif"));
			members.add(createMember("Gabi", "Gwinner", "ruth.ziegler@bbv.ch", "gabi.gif"));

			ueli = members.get(0);
			ruth = members.get(1);
			lukas = members.get(2);
			gabi = members.get(3);
		} catch (IOException e) {
			ueli = null;
			ruth = null;
			lukas = null;
			gabi = null;
			e.printStackTrace();
		}

	}

	private static MemberDto createMember(String firstName, String name, String email, String image) throws IOException {
		MemberDto member = new MemberDto();
		member.setName(name);
		member.setFirstName(firstName);
		member.setEmail(email);
		member.setImage(getImage(image));
		return member;
	}

	public JulklappClient(String uri) {
		this.uri = uri;
		client = Client.create();
		resource = client.resource(uri);
	}

	public void initializeCircles() throws InterruptedException {
		putCircle(CIRCLE_FIRST);
		putCircle(CIRCLE_SECOND);
		putCircle(CIRCLE_THIRD);
		System.out.println("Done [" + resource.getURI().toString() + "]");
	}

	public void initializeMembers() throws InterruptedException {
		initMembers(CIRCLE_FIRST, 2);
		initMembers(CIRCLE_SECOND, 3);
		initMembers(CIRCLE_THIRD, 4);
		System.out.println("Done [" + resource.getURI().toString() + "]");
	}

	public void queryMemberForFirstCircle() {
		MemberDto m1 = queryMember(CIRCLE_FIRST, ueli);
		MemberDto m2 = queryMember(CIRCLE_FIRST, ruth);

		writeImage(m1.getFirstName(), m1.getImage());
		writeImage(m2.getFirstName(), m2.getImage());
	}

	public void queryWichteliForFirstCircle() {
		WichteliDto w1 = queryWichteli(CIRCLE_FIRST, ueli, "ovedvdac");
		WichteliDto w2 = queryWichteli(CIRCLE_FIRST, ruth, "bmzfnrje");

		writeImage("ueli-" + w1.getFirstname(), w1.getImage());
		writeImage("ruth-" + w2.getFirstname(), w2.getImage());
	}

	public void queryWichteliForSecondCircle() {
		WichteliDto w1 = queryWichteli(CIRCLE_SECOND, ueli, "kdbiwrmr");
		WichteliDto w2 = queryWichteli(CIRCLE_SECOND, ruth, "fxgwnpon");
		WichteliDto w4 = queryWichteli(CIRCLE_SECOND, lukas, "nthwyjaj");
	}

	public void queryWichteliForThirdCircle() {
		WichteliDto w1 = queryWichteli(CIRCLE_THIRD, ueli, "kdbiwrmr");
		WichteliDto w2 = queryWichteli(CIRCLE_THIRD, ruth, "fxgwnpon");
		WichteliDto w4 = queryWichteli(CIRCLE_THIRD, lukas, "nthwyjaj");
		WichteliDto w3 = queryWichteli(CIRCLE_THIRD, gabi, "whqjpcbi");
	}

	private void initMembers(String circle, int numOfMembers) {
		for (int i = 0; i < numOfMembers; i++) {
			putMember(resource, circle, members.get(i % members.size()));
		}
	}

	private CircleDto putCircle(String name) {
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

	private WichteliDto queryWichteli(String circle, MemberDto member, String password) {
		WebResource resourceWithPath = resource.path("/circles/" + circle + "/" + member.getFirstName() + "/wichteli");

		CredentialsDto cred = new CredentialsDto();
		cred.setUsername(member.getEmail());
		cred.setPassword(password);

		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		WichteliDto result = builder.post(WichteliDto.class, cred);

		System.out.println("Wichteli of " + member.getFirstName() + " is " + result.getFirstname());
		return result;
	}

	private MemberDto queryMember(String circle, MemberDto member) {
		WebResource resourceWithPath = resource.path("/circles/" + circle + "/" + member.getFirstName());

		Builder builder = resourceWithPath.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		MemberDto result = builder.get(MemberDto.class);

		return member;

	}

	private static byte[] getImage(String image) {
		InputStream is = JulklappClient.class.getClassLoader().getResourceAsStream(image);
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(is);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "gif", baos);
			byte[] imageBytes = baos.toByteArray();
			return imageBytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void writeImage(String name, byte[] image) {
		File file = new File("C:\\Tmp\\julklapp\\" + name + ".gif");
		ByteArrayInputStream bais = new ByteArrayInputStream(image);
		try {
			BufferedImage bufferedImage = ImageIO.read(bais);
			ImageIO.write(bufferedImage, "gif", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Image written to " + file.getAbsolutePath());
	}
}
