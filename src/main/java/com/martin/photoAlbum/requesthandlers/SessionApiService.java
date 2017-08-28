package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.martin.photoAlbum.business.AccountService;

@Path("session")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class SessionApiService extends ApiService<AccountService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionApiService() {
		super(new AccountService());
	}

	@GET
	public Response get() {
		return Response.status(Status.OK).entity(getSession()).build();
	}
}