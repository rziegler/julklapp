package ch.bbv.julklapp.android.rs;

import java.util.List;

import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.http.connector.HttpClientHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ClientResource;

import android.util.Log;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CirclesDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

public class ClientFacade {
	
	private static final String TAG = ClientFacade.class.getSimpleName();

	public ClientFacade(String url) {
		Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
	}

	public CircleDto putCircle(String name) {
		Log.i(TAG, "Create Circle. Name: "+name);
		ClientResource resource = new ClientResource(Config.URL + "circles/" + name);
		CircleDto circle = new CircleDto();
		circle.setName(name);
		resource.setProtocol(Protocol.HTTP);
		CircleDto result = resource.put(circle, CircleDto.class);
		Log.i(TAG, "Circle created. Name: "+result.getName());
		return result;
	}
	
	public MemberDto putMember(String circle, MemberDto member) {
		Log.i(TAG, "Add member to circle. Name: "+circle);
		ClientResource resource = new ClientResource(Config.URL + "circles/" + circle+"/"+member.getFirstName());
		resource.setProtocol(Protocol.HTTP);
		MemberDto memberResult = resource.put(member, MemberDto.class);
		Log.i(TAG, "Circle created. Name: "+memberResult.getName());
		return memberResult;
	}
	
	public CircleDto getCircle(String circle){
		Log.i(TAG, "Retrieve a circle. Name: "+circle);
		ClientResource resource = new ClientResource(Config.URL + "circles/" + circle);
		resource.setProtocol(Protocol.HTTP);
		CircleDto circleResult = resource.get(CircleDto.class);
		return circleResult;
	}

	public List<CircleDto> getCircles() {
		Log.i(TAG, "Retrieve a circles.");
		ClientResource resource = new ClientResource(Config.URL + "circles/");
		resource.setProtocol(Protocol.HTTP);
		CirclesDto circles = resource.get(CirclesDto.class);
		Log.i(TAG, "Number of circles: "+circles.getCircleDto().size());
		return circles.getCircleDto();
	}
	
	public void shuffle(String circle) {
		Log.i(TAG, "Shuffle.");
		ClientResource resource = new ClientResource(Config.URL + "circles/"+circle+"/shuffle");
		resource.setProtocol(Protocol.HTTP);
		resource.get();
	}
	
	public WichteliDto queryWichetli(String circle, String firstname, String email, String password) {
		Log.i(TAG, "Retrieve wichteli.");
		ClientResource resource = new ClientResource(Config.URL +"/circles/" + circle + "/" + firstname + "/wichteli");
		resource.setProtocol(Protocol.HTTP);
	
		CredentialsDto cred = new CredentialsDto();
		cred.setUsername(email);
		cred.setPassword(password);
	
		WichteliDto wichteli = resource.post(cred, WichteliDto.class);
		return wichteli;
	}

	


}
