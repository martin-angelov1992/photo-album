package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.AccountService;

@Path("/logout")
public class LogoutApiService extends ApiService<AccountService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogoutApiService() {
		super(new AccountService());
	}

	@PUT
	public Response logout() {
		webRequest.getSession().removeAttribute("accId");
		
		return Response.ok().build();
	}
}