package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.AccountService.ChangePasswordResult;

@Path("/change-password")
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
public class ChangePasswordApiService extends ApiService<AccountService> {
	private static final long serialVersionUID = 1L;

	public ChangePasswordApiService() {
		super(new AccountService());
	}

	@PUT
	public Response update(@FormParam("password") String password) {
		ChangePasswordResult result = service.changePassword(password);

		if (result == ChangePasswordResult.OK) {
			return Response.ok().build();
		}
	
		return Response.status(400).entity(result).build();
	}
}