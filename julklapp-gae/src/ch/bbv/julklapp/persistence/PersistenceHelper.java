package ch.bbv.julklapp.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ch.bbv.julklapp.core.dao.Circle;
import ch.bbv.julklapp.core.dao.Member;

public class PersistenceHelper {
	
	private final EntityManager entityManager;

	public PersistenceHelper(EntityManager entityManager){
		this.entityManager = entityManager;	
	}
	
	public Circle getCircleByName(String circleName){
		Query query = entityManager
				.createQuery("SELECT c FROM Circle c WHERE c.name = '"
						+ circleName + "'");
		@SuppressWarnings("unchecked")
		List<Circle> circles = query.getResultList();

		if (circles.isEmpty()) {
			return null;
		} else if (circles.size() > 1) {
			throw new IllegalStateException("result must contain exaclty one element.");
		} else {
			return circles.get(0);
		}
	}
	
	public Member getMemberInCircleByName(String circleName, String memberName){
		Circle circle = getCircleByName(circleName);

		if (circle != null) {
			for (Member member : circle.getMembers()) {
				if(member.getFirstName().equals(memberName)) {
					return member;
				}
			}
		} 
		return null;
	}

}