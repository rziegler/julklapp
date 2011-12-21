package ch.bbv.julklapp.persistence;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;

class DatastoreHelper {

	static CircleDto entityToCircleDto(Entity entity) {
		CircleDto result = new CircleDto();
		result.setName((String) entity.getProperty("name"));
		return result;
	}

	static MemberDto entityToMemberDto(Entity entity) {
		MemberDto result = new MemberDto();
		result.setFirstName((String) entity.getProperty("firstName"));
		result.setName((String) entity.getProperty("name"));
		result.setEmail((String) entity.getProperty("email"));
		Blob blob = (Blob) entity.getProperty("image");
		result.setImage(blob.getBytes());
		return result;
	}

	static WichteliDto entityToWichteliDto(Entity entity) {
		String firstName = (String) entity.getProperty("firstName");
		String name = (String) entity.getProperty("name");
		Blob blob = (Blob) entity.getProperty("image");
		WichteliDto result = new WichteliDto(firstName, name, blob.getBytes());
		return result;
	}

	static CredentialsDto entityToCredentialsDto(Entity entity) {
		CredentialsDto result = new CredentialsDto();

		result.setUsername((String) entity.getProperty("email"));
		result.setPassword((String) entity.getProperty("password"));
		return result;
	}
}
