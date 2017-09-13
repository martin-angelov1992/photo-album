package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.martin.photoAlbum.business.CategoryService;

import dto.CategoryWithPathDto;

@Path("/category-with-path")
public class CategoryWithPathApiService extends ApiService<CategoryService> {

	private static final long serialVersionUID = 1L;

	public CategoryWithPathApiService() {
		super(new CategoryService());
	}

	@Path("/{id}")
	@GET
	public Response get(@PathParam("id") int id) {
		CategoryWithPathDto dto = service.getCategoryWithPath(id);

		if (dto == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		return Response.ok().entity(dto).build();
	}
}