package ch.bbv.julklapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import ch.bbv.julklapp.core.dao.Circle;
import ch.bbv.julklapp.core.dao.Member;
import ch.bbv.julklapp.persistence.EMF;
import ch.bbv.julklapp.persistence.PersistenceHelper;

@Path("/circles")
public class CircleResource {
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public Circle createCircle(@PathParam("name") String name, JAXBElement<Circle> circle) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(circle.getValue());
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return circle.getValue();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{name}/delete")
	public String deleteCircle(@PathParam("name") String name) {
		StringBuilder builder = new StringBuilder();
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em
					.createQuery("SELECT c FROM Circle c WHERE c.name = '"
							+ name + "'");
			@SuppressWarnings("unchecked")
			List<Circle> circles = query.getResultList();

			if (circles.isEmpty()) {
				builder.append("No circle found for " + name);
			} else if (circles.size() > 1) {
				builder.append("Multiple circles found for " + name);
			} else {
				em.getTransaction().begin();
				Circle circle = circles.get(0);
				em.remove(circle);
				em.getTransaction().commit();
				builder.append("Circle deleted " + circle.getKey() + ", " + name);
			}
		} finally {
			em.close();
		}
		return builder.toString();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String getCircles() {
		StringBuilder builder = new StringBuilder();
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT c FROM Circle c");
			@SuppressWarnings("unchecked")
			List<Circle> circles = query.getResultList();

			if (circles.isEmpty()) {
				builder.append("No circles yet.");
			}
			for (Circle c : circles) {
				builder.append(c.getName());
				builder.append(", ");
			}
		} finally {
			em.close();
		}
		return builder.toString();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public Circle getCircle(@PathParam("name") String name) {
		StringBuilder builder = new StringBuilder();
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em
					.createQuery("SELECT c FROM Circle c WHERE c.name = '"
							+ name + "'");
			@SuppressWarnings("unchecked")
			List<Circle> circles = query.getResultList();

			if (circles.isEmpty()) {
				builder.append("No circle found for " + name);
			} else if (circles.size() > 1) {
				builder.append("Multiple circles found for " + name);
			} else {
				
				Circle circle = circles.get(0);
				for (Member m : circle.getMembers()) {
					System.out.println(m.getFirstName());
				}
				return circle;
			}
		} finally {
			em.close();
		}
		return null;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{circleName}/{memberName}")
	public Member createMember(@PathParam("circleName") String circleName,@PathParam("memberName") String memberName, JAXBElement<Member> member) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			PersistenceHelper helper= new PersistenceHelper(em);
			Circle circle = helper.getCircleByName(circleName);
			circle.addMember(member.getValue());
			em.getTransaction().begin();
			em.persist(member.getValue());
			em.persist(circle);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return member.getValue();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{circleName}/{memberName}")
	public Member getMember(@PathParam("circleName") String circleName,@PathParam("memberName") String memberName) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			PersistenceHelper helper= new PersistenceHelper(em);
			Member member = helper.getMemberInCircleByName(circleName, memberName);
			return member;
		} finally {
			em.close();
		}
	}
}