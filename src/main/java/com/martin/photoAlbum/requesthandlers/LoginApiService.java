package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.entities.Account;

@Path("/login")
@Produces({ MediaType.APPLICATION_JSON })
public class LoginApiService extends ApiService<AccountService> {

	private static final long serialVersionUID = 1L;

	public LoginApiService() {
		super(new AccountService());
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response login(@FormParam("username") String username, 
			@FormParam("password") String password) {
		Account account = service.login(username, password);
		
		if (account == null) {
			return Response.status(404).entity("NOT_FOUND").build();
		}
		
		return Response.status(200).entity(account).build();
	}
}