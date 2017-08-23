package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.AccountService.EditProfileResult;

@Path("/profile")
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
	public Response update(@FormParam("password") String password, @FormParam("email") String email, 
			@FormParam("name") String name) {
		Session session = getSession();
		
		if (!session.isLoggedIn()) {
			return Response.status(403).entity("NOT_LOGGED_IN").build();
		}
		
		EditProfileResult result = service.editProfile(password, email, name);
		
		if (result != EditProfileResult.OK) {
			Response.status(403).entity(result);
		}
		
		return Response.status(200).entity("OK").build();
	}
}