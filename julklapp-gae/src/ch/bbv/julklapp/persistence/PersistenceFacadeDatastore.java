package ch.bbv.julklapp.persistence;

import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToCircleDto;
import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToCredentialsDto;
import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToMemberDto;
import static ch.bbv.julklapp.persistence.DatastoreHelper.entityToWichteliDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;
import ch.bbv.julklapp.email.EmailNotifier;
import ch.bbv.julklapp.email.GaeEmailNotifier;
import ch.bbv.julklapp.password.CharPasswordGenerator;
import ch.bbv.julklapp.password.PasswordGenerator;
import ch.bbv.julklapp.shuffle.RuthsAlgorithm;
import ch.bbv.julklapp.shuffle.Shuffler;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class PersistenceFacadeDatastore implements PersistenceFacade {

	private static final Logger log = LoggerFactory.getLogger(PersistenceFacadeDatastore.class);
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
		memberEntity.setProperty("image", new Blob(memberDto.getImage()));
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

	@Override
	public void notify(String name) {
		Entity circleEntity = getCircleByName(name);
		List<Entity> membersOfCircle = getMembersOfCircle(circleEntity.getKey());

		EmailNotifier emailNotifier = new GaeEmailNotifier();

		for (Entity memberEntity : membersOfCircle) {
			Entity wichteli = getWichteli((Key) memberEntity.getProperty("wichteliKey"));

			CircleDto circleDto = entityToCircleDto(circleEntity);
			MemberDto memberDto = entityToMemberDto(memberEntity);
			WichteliDto wichteliDto = entityToWichteliDto(wichteli);
			CredentialsDto credentialsDto = entityToCredentialsDto(memberEntity);

			try {
				emailNotifier.sendEmail(circleDto, memberDto, wichteliDto, credentialsDto);
			} catch (Exception e) {
				log.error("Unable to notify member " + memberDto.getEmail(), e);
			}
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

		String password = (String) member.getProperty("password");
		String username = (String) member.getProperty("email");

		boolean usernameEqual = username.equals(credentials.getUsername());
		boolean passwordEqual = password.equals(credentials.getPassword());
		if (usernameEqual && passwordEqual) {
			Entity wichteli = getWichteli((Key) member.getProperty("wichteliKey"));
			WichteliDto result = entityToWichteliDto(wichteli);
			return result;
		}

		String msg;
		if (!usernameEqual) {
			msg = String
					.format("Username not equal. Expected '%s', but was '%s'.", username, credentials.getUsername());
		} else {
			msg = String
					.format("Password not equal. Expected '%s', but was '%s'.", password, credentials.getPassword());
		}
		log.debug(msg);
		throw new IllegalStateException("Invalid credentials.");
	}

	private Entity getWichteli(Key key) {
		Query q = new Query();
		q.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	@Override
	public Image getMemberImageInCircle(String circleName, String memberName, int level) {
		Entity member = getMemberInCircleByName(circleName, memberName);

		Blob imageBlob = (Blob) member.getProperty("image");
		Image image = ImagesServiceFactory.makeImage(imageBlob.getBytes());
		image = transformImage(image, level);

		return image;
	}

	private Image transformImage(Image image, int level) {
		Image transformedImage = image;
		if (level > 0) {
			Transform vertikalFlip = ImagesServiceFactory.makeVerticalFlip();
			transformedImage = ImagesServiceFactory.getImagesService().applyTransform(vertikalFlip, image);
		}
		return transformedImage;
	}
}
