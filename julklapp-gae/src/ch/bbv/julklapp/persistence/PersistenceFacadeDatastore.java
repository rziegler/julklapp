package ch.bbv.julklapp.persistence;

import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToCircleDto;
import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToMemberDto;
import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToWichtelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;
import ch.bbv.julklapp.password.CharPasswordGenerator;
import ch.bbv.julklapp.password.PasswordGenerator;
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

	private static final Logger log = Logger.getLogger(PersistenceFacadeDatastore.class.getName());
	private final DatastoreService datastore;

	public PersistenceFacadeDatastore() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	@Override
	public CircleDto getCircleDtoByName(String circleName) {
		Entity circleEntity = getCircleByName(circleName);
		CircleDto circleDto = entityToCircleDto(circleEntity);

		List<Entity> memberEntities = getMembersOfCircle(circleEntity.getKey());
		for (Entity memberEntity : memberEntities) {
			circleDto.addMember(entityToMemberDto(memberEntity));
		}
		return circleDto;
	}

	private Entity getCircleByName(String circleName) {
		Query q = new Query("Circle");
		q.addFilter("name", Query.FilterOperator.EQUAL, circleName);

		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	@Override
	public MemberDto getMemberDtoInCircleByName(String circleName, String memberName) {
		Entity entity = getMemberInCircleByName(circleName, memberName);
		return entityToMemberDto(entity);
	}

	private Entity getMemberInCircleByName(String circleName, String memberName) {
		Entity circleEntity = getCircleByName(circleName);

		Query q = new Query("Member");
		q.addFilter("circleKey", Query.FilterOperator.EQUAL, circleEntity.getKey());
		q.addFilter("firstName", Query.FilterOperator.EQUAL, memberName);

		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	@Override
	public CircleDto createCircle(CircleDto circleDto) {
		Entity circle = new Entity("Circle");
		circle.setProperty("name", circleDto.getName());
		datastore.put(circle);
		return circleDto;
	}

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

	@Override
	public CircleDto deleteCircleDto(String name) {
		Entity circleEntity = getCircleByName(name);
		datastore.delete(circleEntity.getKey());
		return entityToCircleDto(circleEntity);
	}

	@Override
	public void shuffle(String name) {
		Entity circleEntity = getCircleByName(name);
		List<Entity> membersOfCircle = getMembersOfCircle(circleEntity.getKey());

		Shuffler<Entity> shuffler = new RuthsAlgorithm<Entity>();
		PasswordGenerator generator = new CharPasswordGenerator();

		List<Entry<Entity, Entity>> shuffles = shuffler.shuffle(membersOfCircle);
		for (Entry<Entity, Entity> entry : shuffles) {
			Entity member = entry.getKey();
			Entity wichteli = entry.getValue();
			String password = generator.generatePassword();

			log.info(String.format("Member %s -> Wichteli %s [%s]", member.getProperty("firstName"),
					wichteli.getProperty("firstName"), password));

			member.setProperty("password", password);
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

	@Override
	public WichteliDto getWichteli(String name, String memberName, CredentialsDto credentials) {
		Entity member = getMemberInCircleByName(name, memberName);

		if (member.getProperty("password").equals(credentials.getPassword())
				&& member.getProperty("email").equals(credentials.getUsername())) {
			Entity wichteli = getWichteli((Key) member.getProperty("wichteliKey"));
			WichteliDto result = entityToWichtelDto(wichteli);
			return result;
		}
		throw new IllegalStateException("Invalid credentials.");
	}

	private Entity getWichteli(Key key) {
		Query q = new Query();
		q.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}
}
