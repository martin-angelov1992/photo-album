package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.PhotoService;
import com.martin.photoAlbum.entities.Photo;

@Path("/thumbnail")
@Produces("image/jpg")
public class ThumbnailApiService extends ApiService<PhotoService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThumbnailApiService() {
		super(new PhotoService(null));
	}

	@Path("/{id}")
	@GET
	public Response get(@PathParam("id") int id) {
		Photo photo = service.getById(id);
		
		if (photo == null) {
			return Response.status(404).build();
		}
		
		return Response.ok().entity(photo.getThumbnail()).build();
	}
}
