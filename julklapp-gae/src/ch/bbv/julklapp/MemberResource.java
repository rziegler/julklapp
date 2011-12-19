package ch.bbv.julklapp;
 
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import ch.bbv.julklapp.core.dao.Circle;
import ch.bbv.julklapp.core.dao.Member;
import ch.bbv.julklapp.persistence.EMF;
import ch.bbv.julklapp.persistence.PersistenceFacade;

@Path("/members")
public class MemberResource {

	
}
