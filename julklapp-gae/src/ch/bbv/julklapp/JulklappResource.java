package ch.bbv.julklapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/test")
public class JulklappResource {

	@GET
	@Produces("text/plain")
	public String getClichedMessage() {
		return "Hello World";
	}
	
	@GET
	@Produces("text/plain")
	@Path("/thursday/")
	public String getThursday() {
		return "Hello Thursday";
	}
	
	@GET
	@Produces("text/plain")
	@Path("/hello/{name}")
	public String sayHello(@PathParam("name") String name) {
		return "Hello " + name;
	}
	
}
