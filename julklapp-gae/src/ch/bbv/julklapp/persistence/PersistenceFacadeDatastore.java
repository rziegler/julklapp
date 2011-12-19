package ch.bbv.julklapp.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;
import ch.bbv.julklapp.shuffle.RuthsAlgorithm;
import ch.bbv.julklapp.shuffle.Shuffler;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class PersistenceFacadeDatastore implements PersistenceFacade {

	private DatastoreService datastore;
	private Object entity;

	public PersistenceFacadeDatastore() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#getCircleDtoByName(java
	 * .lang.String)
	 */
	@Override
	public CircleDto getCircleDtoByName(String circleName) {
		Entity entity = getCircleByName(circleName);
		return entityToCircleDto(entity);
	}

	private Entity getCircleByName(String circleName) {
		Query q = new Query("Circle");
		q.addFilter("name", Query.FilterOperator.EQUAL, circleName);

		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#getMemberDtoInCircleByName
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public MemberDto getMemberDtoInCircleByName(String circleName,
			String memberName) {
		Entity entity = getMemberInCircleByName(circleName, memberName);
		return entityToMemberDto(entity);
	}

	private Entity getMemberInCircleByName(String circleName, String memberName) {
		Entity circleEntity = getCircleByName(circleName);

		Query q = new Query("Member");
		q.addFilter("circleKey", Query.FilterOperator.EQUAL,
				circleEntity.getKey());
		q.addFilter("firstName", Query.FilterOperator.EQUAL, memberName);

		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#createCircle(ch.bbv.julklapp
	 * .dto.CircleDto)
	 */
	@Override
	public CircleDto createCircle(CircleDto circleDto) {
		Entity circle = new Entity("Circle");
		circle.setProperty("name", circleDto.getName());
		datastore.put(circle);
		return circleDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#createMember(java.lang
	 * .String, ch.bbv.julklapp.dto.MemberDto)
	 */
	@Override
	public MemberDto createMember(String circleName, MemberDto memberDto) {
		Entity circleEntity = getCircleByName(circleName);

		Entity memberEntity = new Entity("Member");
		memberEntity.setProperty("firstName", memberDto.getFirstName());
		memberEntity.setProperty("name", memberDto.getName());
		memberEntity.setProperty("email", memberDto.getEmail());
		memberEntity.setProperty("image", memberDto.getImage());
		memberEntity.setProperty("circleKey", circleEntity.getKey());

		datastore.put(memberEntity);
		return memberDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.bbv.julklapp.persistence.PersistenceFacadeIf#getCircleDtos()
	 */
	@Override
	public List<CircleDto> getCircleDtos() {
		List<CircleDto> result = new ArrayList<CircleDto>();
		Query q = new Query("Circle");

		PreparedQuery pq = datastore.prepare(q);
		for (Entity entity : pq.asIterable()) {
			result.add(entityToCircleDto(entity));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#deleteCircleDto(java.
	 * lang.String)
	 */
	@Override
	public CircleDto deleteCircleDto(String name) {
		Entity circleEntity = getCircleByName(name);
		datastore.delete(circleEntity.getKey());
		return entityToCircleDto(circleEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#shuffle(java.lang.String)
	 */
	@Override
	public void shuffle(String name) {
		Entity circleEntity = getCircleByName(name);
		List<Entity> membersOfCircle = getMembersOfCircle(circleEntity.getKey());

		Shuffler<Entity> shuffler = new RuthsAlgorithm<Entity>();

		List<Entry<Entity, Entity>> shuffles = shuffler
				.shuffle(membersOfCircle);
		for (Entry<Entity, Entity> entry : shuffles) {
			Entity member = entry.getKey();
			Entity wichteli = entry.getValue();
			member.setProperty("password", "test123");
			member.setProperty("wichteliKey", wichteli.getKey());
			datastore.put(member);
		}
	}

	private List<Entity> getMembersOfCircle(Key circleKey) {
		Query q = new Query("Member");
		q.addFilter("circleKey", Query.FilterOperator.EQUAL, circleKey);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asList(FetchOptions.Builder.withDefaults());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.bbv.julklapp.persistence.PersistenceFacadeIf#getWichteli(java.lang
	 * .String, java.lang.String, ch.bbv.julklapp.dto.CredentialsDto)
	 */
	@Override
	public WichteliDto getWichteli(String name, String memberName,
			CredentialsDto value) {
		Entity member = getMemberInCircleByName(name, memberName);

		if (member.getProperty("password").equals(value.getPassword())
				&& member.getProperty("email").equals(value.getUsername())) {

			Entity wichteli = getWichteli((Key) member
					.getProperty("wichteliKey"));

			WichteliDto result = entityToWichtelDto(wichteli); 

			return result;
		}
		throw new IllegalStateException("Invalid credentials.");
	}

	private WichteliDto entityToWichtelDto(Entity entity) {
		WichteliDto result = new WichteliDto(
				(String) entity.getProperty("firstName"),
				(String) entity.getProperty("name"));
		return result;
	}

	private Entity getWichteli(Key key) {
		Query q = new Query();
		q.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL,
				key);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	private CircleDto entityToCircleDto(Entity entity) {
		CircleDto result = new CircleDto();
		result.setName((String) entity.getProperty("name"));
		return result;
	}

	private MemberDto entityToMemberDto(Entity entity) {
		MemberDto result = new MemberDto();
		result.setFirstName((String) entity.getProperty("firstName"));
		result.setName((String) entity.getProperty("name"));
		result.setEmail((String) entity.getProperty("email"));
		result.setImage((String) entity.getProperty("image"));
		return result;
	}

}
