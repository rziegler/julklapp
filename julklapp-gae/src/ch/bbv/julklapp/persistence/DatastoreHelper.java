package ch.bbv.julklapp.persistence;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

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
		result.setImage((String) entity.getProperty("image"));
		return result;
	}

	static WichteliDto entityToWichtelDto(Entity entity) {
		String firstName = (String) entity.getProperty("firstName");
		String name = (String) entity.getProperty("name");
		WichteliDto result = new WichteliDto(firstName, name);
		return result;
	}
}
