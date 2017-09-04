package com.martin.photoAlbum.requesthandlers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

@Path("/front-end")
public class FrontEndService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GET
	@Path("/{request : .+}")
	public Response handle(@PathParam("request") String request) throws IOException {
		String mediType = getMediaType(request);
		File file = new File("front-end/"+request);
		
		String content = "";
		if (file.exists()) {
			content = IOUtils.toString(new FileReader(file));	
		}
		
		return Response.status(200).type(mediType).entity(content).build();
	}

	private String getMediaType(String request) {
		String parts[] = request.split("\\.");

		String extension = parts[parts.length-1];
		
		if ("css".equals(extension)) {
			return "text/css";
		}

		return MediaType.TEXT_HTML;
	}
}