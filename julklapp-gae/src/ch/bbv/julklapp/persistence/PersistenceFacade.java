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
import ch.bbv.julklapp.dto.MemberDto;

public class PersistenceFacade {

	private final EntityManager entityManager;
	private final CircleTransformer circleTransformer;
	private final MemberTransformer memberTransformer;

	public PersistenceFacade(EntityManager entityManager) {
		this.entityManager = entityManager;
		circleTransformer = new CircleTransformer();
		memberTransformer = new MemberTransformer();
	}

	public CircleDto getCircleDtoByName(String circleName) {
		return circleTransformer.unmarshal(getCircleByName(circleName, false));
	}

	private Circle getCircleByName(String circleName, boolean loadMembers) {
		Query query = entityManager
				.createQuery("SELECT c FROM Circle c WHERE c.name = '"
						+ circleName + "'");
		@SuppressWarnings("unchecked")
		List<Circle> circles = query.getResultList();

		if (circles.isEmpty()) {
			return null;
		} else if (circles.size() > 1) {
			throw new IllegalStateException(
					"result must contain exaclty one element.");
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

	public MemberDto getMemberDtoInCircleByName(String circleName,
			String memberName) {
		return memberTransformer.unmarshal(getMemberInCircleByName(circleName,
				memberName));
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

	public CircleDto createCircle(CircleDto circleDto) {
		try {
			entityManager.getTransaction().begin();
			Circle circle = circleTransformer.marshal(circleDto);
			entityManager.persist(circle);
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
		return circleDto;
	}

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
		} finally {
			entityManager.close();
		}
		return memberDto;
	}

	public List<CircleDto> getCircleDtos() {
		List<CircleDto> result = new ArrayList<CircleDto>();
		try {
			Query query = entityManager.createQuery("SELECT c FROM Circle c");
			@SuppressWarnings("unchecked")
			List<Circle> circles = query.getResultList();

			for (Circle c : circles) {
				result.add(circleTransformer.unmarshal(c));
			}
		} finally {
			entityManager.close();
		}
		return result;
	}

	public CircleDto deleteCircleDto(String name) {
		Circle deletedCircle;
		try {
			Query query = entityManager
					.createQuery("SELECT c FROM Circle c WHERE c.name = '"
							+ name + "'");
			@SuppressWarnings("unchecked")
			List<Circle> circles = query.getResultList();

			entityManager.getTransaction().begin();
			deletedCircle = circles.get(0);
			entityManager.remove(deletedCircle);
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}

		if (deletedCircle != null) {
			return circleTransformer.unmarshal(deletedCircle);
		}
		return null;
	}

}
