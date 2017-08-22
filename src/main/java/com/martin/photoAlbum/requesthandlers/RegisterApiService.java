package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.AccountService.RegisterResult;
import com.martin.photoAlbum.business.AccountService.RegisterStatus;

@Path("/register")
@Produces({ MediaType.APPLICATION_JSON })
public class RegisterApiService extends ApiService<AccountService> {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegisterApiService() {
		super(new AccountService());
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    public Response register(@FormParam("username") String username, 
    		 			     @FormParam("password") String password, 
    		 			     @FormParam("email") String email, 
    		 			     @FormParam("name") String name) {
		updateSession();
		
		RegisterResult result = service.register(username, password, email, name);
		
		if (result.getStatus() != RegisterStatus.OK) {
			return Response.status(400).entity(result.getStatus()).build();
		}
		
    	return Response.status(200).entity(result.getAccount()).build();
    }
}