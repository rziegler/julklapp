package ch.bbv.julklapp.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ch.bbv.julklapp.core.dao.Circle;
import ch.bbv.julklapp.core.dao.Member;
import ch.bbv.julklapp.core.dao.transformer.CircleTransformer;
import ch.bbv.julklapp.core.dao.transformer.MemberTransformer;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

import com.google.appengine.api.images.Image;

public class PersistenceFacadeJPA implements PersistenceFacade {

	private final EntityManager entityManager;
	private final CircleTransformer circleTransformer;
	private final MemberTransformer memberTransformer;

	public PersistenceFacadeJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
		circleTransformer = new CircleTransformer();
		memberTransformer = new MemberTransformer();
	}

	@Override
	public CircleDto getCircleDtoByName(String circleName) {
		return circleTransformer.unmarshal(getCircleByName(circleName, false));
	}

	private Circle getCircleByName(String circleName, boolean loadMembers) {
		Query query = entityManager.createQuery("SELECT c FROM Circle c WHERE c.name = '" + circleName + "'");
		@SuppressWarnings("unchecked")
		List<Circle> circles = query.getResultList();

		if (circles.isEmpty()) {
			return null;
		} else if (circles.size() > 1) {
			throw new IllegalStateException("result must contain exaclty one element.");
		} else {
			Circle circle = circles.get(0);
			if (loadMembers) { // lazily load all member
				for (Member m : circle.getMembers()) {
					m.getFirstName();
				}
			}
			return circle;
		}
	}

	@Override
	public MemberDto getMemberDtoInCircleByName(String circleName, String memberName) {
		return memberTransformer.unmarshal(getMemberInCircleByName(circleName, memberName));
	}

	private Member getMemberInCircleByName(String circleName, String memberName) {
		Circle circle = getCircleByName(circleName, true);

		if (circle != null) {
			for (Member member : circle.getMembers()) {
				if (member.getFirstName().equals(memberName)) {
					return member;
				}
			}
		}
		return null;
	}

	@Override
	public CircleDto createCircle(CircleDto circleDto) {
		entityManager.getTransaction().begin();
		Circle circle = circleTransformer.marshal(circleDto);
		entityManager.persist(circle);
		entityManager.getTransaction().commit();
		return circleDto;
	}

	@Override
	public MemberDto createMember(String circleName, MemberDto memberDto) {
		try {
			Member member = memberTransformer.marshal(memberDto);
			Circle circle = getCircleByName(circleName, true);
			circle.addMember(member);
			entityManager.getTransaction().begin();
			entityManager.persist(member);
			entityManager.persist(circle);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
		return memberDto;
	}

	@Override
	public List<CircleDto> getCircleDtos() {
		List<CircleDto> result = new ArrayList<CircleDto>();
		Query query = entityManager.createQuery("SELECT c FROM Circle c");
		@SuppressWarnings("unchecked")
		List<Circle> circles = query.getResultList();

		for (Circle c : circles) {
			result.add(circleTransformer.unmarshal(c));
		}

		return result;
	}

	@Override
	public CircleDto deleteCircleDto(String name) {
		Circle deletedCircle;

		Query query = entityManager.createQuery("SELECT c FROM Circle c WHERE c.name = '" + name + "'");
		@SuppressWarnings("unchecked")
		List<Circle> circles = query.getResultList();

		entityManager.getTransaction().begin();
		deletedCircle = circles.get(0);
		entityManager.remove(deletedCircle);
		entityManager.getTransaction().commit();

		if (deletedCircle != null) {
			return circleTransformer.unmarshal(deletedCircle);
		}
		return null;
	}

	@Override
	public void shuffle(String name) {
		entityManager.getTransaction().begin();
		Circle circle = getCircleByName(name, true);
		// Shuffler shuffler = new RuthsAlgorithm();
		// DEV-woRuthGradDranIst shuffler.shuffle(circle);
		for (Member member : circle.getMembers()) {
			entityManager.persist(member);
		}
		// entityManager.persist(circle);
		entityManager.getTransaction().commit();
	}

	@Override
	public WichteliDto getWichteli(String name, String memberName, CredentialsDto value) {
		Member member = getMemberInCircleByName(name, memberName);
		if (member.getPassword().equals(value.getPassword()) && member.getEmail().equals(value.getUsername())) {
			WichteliDto result = new WichteliDto(member.getWichteli().getFirstName(), member.getWichteli().getName());
			return result;
		}
		throw new IllegalStateException("Invalid credentials.");
	}

	@Override
	public void notify(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getMemberImageInCircle(String circleName, String memberName, int level) {
		// TODO Auto-generated method stub
		return null;
	}
}
