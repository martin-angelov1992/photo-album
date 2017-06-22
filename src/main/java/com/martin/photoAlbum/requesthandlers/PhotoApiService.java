package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

public class PhotoApiService {
	@POST
	public Response add(int categoryID, String name, String description) {
		
	}
	
	@PUT
	public Response edit(int id, String name, String description) {
		
	}
	
	@DELETE
	public Response delete(int id) {
		
	}
}