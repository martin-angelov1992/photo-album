package com.martin.photoAlbum.requesthandlers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.martin.photoAlbum.business.CategoryService;
import com.martin.photoAlbum.business.CategoryService.DeleteResult;
import com.martin.photoAlbum.business.CategoryService.EditResult;
import com.martin.photoAlbum.entities.Category;

@Path("/category")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class CategoryApiService {
	private CategoryService service = new CategoryService();
	
	@GET
	@Path("/{id}")
	public Category get(@PathParam("id") int id) {
		return null;
	}
	
	@DELETE
	public DeleteResult delete(int id) {
		return null;
	}
	
	@POST
	public Integer add(String name) {
		return null;
	}
	
	@PUT
	public EditResult edit(String newName) {
		return null;
	}
	
	@GET
	public List<Category> getAll() {
		return null;
	}
}
