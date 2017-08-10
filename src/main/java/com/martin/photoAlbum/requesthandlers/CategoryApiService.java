package com.martin.photoAlbum.requesthandlers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.ContainerRequest;

import com.martin.photoAlbum.business.CategoryService;
import com.martin.photoAlbum.business.CategoryService.DeleteResult;
import com.martin.photoAlbum.business.CategoryService.EditResult;
import com.martin.photoAlbum.entities.Category;

@Path("/category")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class CategoryApiService extends ApiService<CategoryService> {
	private static final long serialVersionUID = 1L;
	@Context
    HttpServletRequest webRequest2;
	@Context Request request;
	

	public CategoryApiService() {
		super(new CategoryService());
	}
    
	@GET
	@Path("/{id}")
	public Response get(@Context HttpServletRequest webRequest, @Context HttpServletResponse webResponse) {
		updateSession();
		
		((ContainerRequest)request).getPropertyNames();
		return null;
		//return service.getById(id);
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
