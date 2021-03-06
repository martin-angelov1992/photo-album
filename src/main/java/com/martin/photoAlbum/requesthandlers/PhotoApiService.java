package com.martin.photoAlbum.requesthandlers;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
import com.martin.photoAlbum.business.PhotoService.DeleteResult;
import com.martin.photoAlbum.business.PhotoService.EditResult;

import dto.PhotoDto;

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
		AddResult result;
		
		try {
			byte[] bytes = IOUtils.toByteArray(uploadedInputStream);
			
			result = service.add(categoryID, name, description, bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		if (result.getStatus() != AddResult.Status.OK) {
			return Response.status(403).entity(result.getStatus()).build();
		}
		
		return Response.ok().entity(result.getId()).build();
	}
	
	@PUT
	@Path("/{id}")
	public Response edit(@PathParam("id") int id, 
			@FormParam("name") String name, 
			@FormParam("description") String description) {
		EditResult result = service.edit(id, name, description);
		
		if (result != EditResult.OK) {
			return Response.status(403).entity(result).build();
		}

		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") int id) {
		DeleteResult result = service.delete(id);

		if (result != DeleteResult.OK) {
			return Response.status(403).entity(result).build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id) {
		PhotoDto photo = service.getDtoById(id);

		if (photo == null) {
			return Response.status(404).build();
		}

		return Response.ok().entity(photo).build();
	}
}