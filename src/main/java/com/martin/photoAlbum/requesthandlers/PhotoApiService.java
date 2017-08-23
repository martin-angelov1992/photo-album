package com.martin.photoAlbum.requesthandlers;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import com.martin.photoAlbum.business.PhotoService;
import com.martin.photoAlbum.business.PhotoService.AddResult;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

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
	public Response add(int categoryID, String name, String description, 
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		updateSession();
		
		AddResult result;
		
		try {
			result = service.add(categoryID, name, description, IOUtils.toByteArray(uploadedInputStream));
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		if (result != AddResult.OK) {
			return Response.status(403).entity(result).build();
		}
		
		return Response.ok().build();
	}
	
	@PUT
	public Response edit(int id, String name, String description) {
		updateSession();
		
		service.edit(id, name, description);
		return null;
	}
	
	@DELETE
	public Response delete(int id) {
		updateSession();
		return null;
	}
}