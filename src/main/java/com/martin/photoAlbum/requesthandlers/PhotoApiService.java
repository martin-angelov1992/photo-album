package com.martin.photoAlbum.requesthandlers;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.martin.photoAlbum.business.PhotoService;
import com.martin.photoAlbum.business.PhotoService.AddResult;

@Path("/manage-photo")
public class PhotoApiService extends ApiService<PhotoService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhotoApiService() {
		super(new PhotoService(null));
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response add(@FormDataParam("categoryID") int categoryID, 
			@FormDataParam("name") String name, 
			@FormDataParam("description") String description, 
			@FormDataParam("file") InputStream uploadedInputStream) {
		updateSession();
		
		AddResult result;
		
		try {
			result = service.add(categoryID, name, description, IOUtils.toByteArray(uploadedInputStream));
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		if (result.getStatus() != AddResult.Status.OK) {
			return Response.status(403).entity(result).build();
		}
		
		return Response.ok().entity(result.getId()).build();
	}
	
	@PUT
	@Path("/{id}")
	public Response edit(@PathParam("id") int id, 
			@FormParam("name") String name, 
			@FormParam("description") String description) {
		updateSession();
		
		service.edit(id, name, description);
		return null;
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") int id) {
		updateSession();
		return null;
	}
}