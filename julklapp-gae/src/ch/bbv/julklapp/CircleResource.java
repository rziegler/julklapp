package ch.bbv.julklapp;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;
import ch.bbv.julklapp.persistence.PersistenceFacade;
import ch.bbv.julklapp.persistence.PersistenceFacadeDatastore;

import com.google.appengine.api.images.Image;

@Path("/circles")
public class CircleResource {

	private final PersistenceFacade facade;

	public CircleResource() {
		facade = new PersistenceFacadeDatastore();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public CircleDto createCircle(@PathParam("name") String name, JAXBElement<CircleDto> circle) {
		return facade.createCircle(circle.getValue());
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{name}/shuffle")
	public String shuffle(@PathParam("name") String name) {
		facade.shuffle(name);
		facade.notify(name);
		return "HALLO";
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{name}/{memberName}/wichteli")
	public WichteliDto getWichteli(@PathParam("name") String name, @PathParam("memberName") String memberName,
			JAXBElement<CredentialsDto> credentials) {
		return facade.getWichteli(name, memberName, credentials.getValue());
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
	public MemberDto createMember(@PathParam("circleName") String circleName,
			@PathParam("memberName") String memberName, JAXBElement<MemberDto> member) {
		return facade.createMember(circleName, member.getValue());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{circleName}/{memberName}")
	public MemberDto getMember(@PathParam("circleName") String circleName, @PathParam("memberName") String memberName) {
		return facade.getMemberDtoInCircleByName(circleName, memberName);
	}

	@GET
	@Consumes(MediaType.TEXT_HTML)
	@Produces("image/*")
	@Path("/{circleName}/{memberName}/image")
	public Response getImage(@PathParam("circleName") String circleName, @PathParam("memberName") String memberName,
			@DefaultValue("0") @QueryParam("level") int level) {
		// TODO Use enum as param type for level, see Example 2.15. in
		// http://jersey.java.net/nonav/documentation/latest/user-guide.html
		Image image = facade.getMemberImageInCircle(circleName, memberName, level);

		if (image == null || image.getImageData() == null) {
			// TODO: Example 2.28./2.29.
			throw new WebApplicationException(404);
		}

		String mediaType = "image/" + image.getFormat().name();
		return Response.ok(image.getImageData(), mediaType).build();
	}
}
