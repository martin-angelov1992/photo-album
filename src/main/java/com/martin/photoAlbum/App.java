package com.martin.photoAlbum;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {
    	RequestHandlers requestHandlers = RequestHandlers.getInstance();
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        ResourceConfig config = new ResourceConfig(requestHandlers.getAll());
        Server server = JettyHttpContainerFactory.createServer(baseUri, config);
        server.start();
    }
}
