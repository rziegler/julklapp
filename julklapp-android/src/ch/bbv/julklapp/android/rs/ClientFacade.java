package ch.bbv.julklapp.android.rs;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.http.connector.HttpClientHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import android.util.Log;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.dto.CircleDto;

public class ClientFacade {

	public ClientFacade(String url) {
		Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
	}

	public void putCircle(String name) {

		ClientResource resource = new ClientResource(Config.URL + "circles/" + name);

		CircleDto circle = new CircleDto();
		circle.setName(name);

		resource.setProtocol(Protocol.HTTP);

		Representation representation = resource.put(circle, MediaType.APPLICATION_JSON);
		JacksonConverter converter = new JacksonConverter();
		CircleDto object;
		try {
			object = converter.toObject(representation, CircleDto.class, resource);
			Log.i("EinTest", "Circle created. Name: "+object.getName());
		} catch (IOException e) {
			Log.e("einTest", e.getMessage());
			e.printStackTrace();
		}

	}

}
