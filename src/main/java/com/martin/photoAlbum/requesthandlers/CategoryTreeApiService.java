package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.CategoryService;

@Path("/category-tree")
public class CategoryTreeApiService extends ApiService<CategoryService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoryTreeApiService() {
		super(new CategoryService());
	}

	@GET
	public Response get() {
		return Response.status(200).entity(service.getCategoryTree()).build();
	}
}