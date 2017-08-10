package com.martin.photoAlbum.requesthandlers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Param;

import com.martin.photoAlbum.business.CategoryService;
import com.martin.photoAlbum.business.CategoryService.AddCategoryResult;
import com.martin.photoAlbum.business.CategoryService.DeleteResult;
import com.martin.photoAlbum.business.CategoryService.EditResult;
import com.martin.photoAlbum.entities.Category;

@Path("/category")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class CategoryApiService extends ApiService<CategoryService> {
	private static final long serialVersionUID = 1L;

	public CategoryApiService() {
		super(new CategoryService());
	}
    
	@GET
	@Path("/{id}")
	public Category get(@Param int id) {
		updateSession();
		
		return service.getById(id);
	}
	
	@DELETE
	public DeleteResult delete(int id) {
		return null;
	}
	
	@POST
	public Response add(@Param String name) {
		AddCategoryResult result = service.add(name);
		
		return Response.status(200).entity(result.getNewId()).build();
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
