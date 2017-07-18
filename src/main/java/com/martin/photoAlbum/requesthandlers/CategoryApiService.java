package com.martin.photoAlbum.requesthandlers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.martin.photoAlbum.entities.Category;

@Path("category")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class CategoryApiService {
	@GET
	public Category get(int id) {
		
	}
	
	@DELETE
	public DeleteResult delete(int id) {
		
	}
	
	@POST
	public Integer add(String name) {
		
	}
	
	@PUT
	public EditResult edit(String newName) {
		
	}
	
	@GET
	public List<Category> getAll() {
		
	}
}
