package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Param;

import com.martin.photoAlbum.business.CategoryService;
import com.martin.photoAlbum.business.CategoryService.AddCategoryResult;
import com.martin.photoAlbum.business.CategoryService.DeleteResult;
import com.martin.photoAlbum.business.CategoryService.EditResult;
import com.martin.photoAlbum.entities.Category;

@Path("/category")
public class CategoryApiService extends ApiService<CategoryService> {
	private static final long serialVersionUID = 1L;

	public CategoryApiService() {
		super(new CategoryService());
	}
    
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id) {
		updateSession();
		
		Category category = service.getById(id);
		
		if (category == null) {
			return Response.status(404).entity("NOT_FOUND").build();
		}
		
		return Response.status(200).entity(category).build();
	}
	
	@DELETE
	public Response delete(@PathParam("id") int id) {
		updateSession();

		Category category = service.getById(id);
		
		if (category == null) {
			return Response.status(404).build();
		}
		
		DeleteResult result = service.delete(id);
		
		if (result != DeleteResult.OK) {
			return Response.status(403).entity(result).build();
		}

		return Response.status(200).build();
	}
	
	@POST
	public Response add(@Param String name) {
		AddCategoryResult result = service.add(name);
		
		return Response.status(200).entity(result.getNewId()).build();
	}
	
	@PUT
	public Response edit(@PathParam("id") int id, @Param String newName) {
		updateSession();

		Category category = service.getById(id);
		
		if (category == null) {
			return Response.status(404).build();
		}
		
		EditResult result = service.edit(id, newName);
		
		if (result != EditResult.OK) {
			return Response.status(403).entity(result).build();
		}
		
		return Response.status(200).build();
	}
	
	@GET
	public Response getAll() {
		return Response.status(200).entity(service.getAll()).build();
	}
}
