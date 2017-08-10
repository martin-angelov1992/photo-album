package com.martin.photoAlbum;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        ResourceConfig config = new ResourceConfig();
        config.packages("com.martin.photoAlbum.requesthandlers");
        ServletHolder servlet = new MyServletHolder(new ServletContainer(config));

        Server server = new Server(9998);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        context.setHandler(sessions);
        
        server.start();
    }
}
