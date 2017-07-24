package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("register")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class RegisterApiService {
	 
    @POST
    public Response register(@PathParam("username") String username, 
    		 			     @PathParam("password") String password, 
    		 			     @PathParam("email") String email, 
    		 			     @PathParam("name") String name) {
    	return null;
    }
}