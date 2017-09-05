package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.AccountService.EditProfileResult;

@Path("/profile")
@Produces({ MediaType.APPLICATION_JSON })
public class EditProfileApiService extends ApiService<AccountService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditProfileApiService() {
		super(new AccountService());
	}
	
	@GET
	public Response get() {
		Session session = getSession();
		
		if (!session.isLoggedIn()) {
			return Response.status(403).entity("NOT_LOGGED_IN").build();
		}
		
		return Response.status(200).entity(session.getAccount()).build();
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response update(@FormDataParam("email") String email, 
			@FormDataParam("name") String name) {
		EditProfileResult result = service.editProfile(email, name);
		
		if (result != EditProfileResult.OK) {
			return Response.status(403).entity(result).build();
		}
		
		return Response.status(200).entity("OK").build();
	}
}