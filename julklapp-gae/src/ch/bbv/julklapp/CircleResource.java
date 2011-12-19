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
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.persistence.EMF;
import ch.bbv.julklapp.persistence.PersistenceFacade;

@Path("/circles")
public class CircleResource {
	
	private PersistenceFacade facade;
	
	public CircleResource() {
		facade = new PersistenceFacade(EMF.get().createEntityManager());
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public CircleDto createCircle(@PathParam("name") String name, JAXBElement<CircleDto> circle) {
		return facade.createCircle(circle.getValue());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}/delete")
	public CircleDto deleteCircle(@PathParam("name") String name) {
		return facade.deleteCircleDto(name);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public List<CircleDto> getCircles() {
		return facade.getCircleDtos();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public CircleDto getCircle(@PathParam("name") String name) {
		return facade.getCircleDtoByName(name);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{circleName}/{memberName}")
	public MemberDto createMember(@PathParam("circleName") String circleName,@PathParam("memberName") String memberName, JAXBElement<MemberDto> member) {
		return facade.createMember(circleName, member.getValue());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{circleName}/{memberName}")
	public MemberDto getMember(@PathParam("circleName") String circleName,@PathParam("memberName") String memberName) {
			return facade.getMemberDtoInCircleByName(circleName, memberName);
	}
}
