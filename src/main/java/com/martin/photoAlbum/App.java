package com.martin.photoAlbum;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws IOException {
        System.out.println("Starting Crunchify's Embedded Jersey HTTPServer...\n");
        HttpServer crunchifyHTTPServer = createHttpServer();
        crunchifyHTTPServer.start();
        System.out.println(String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n", getCrunchifyURI()));
        System.out.println("Started Crunchify's Embedded Jersey HTTPServer Successfully !!!");
    }
 
    private static HttpServer createHttpServer() throws IOException {
        ResourceConfig crunchifyResourceConfig = new PackagesResourceConfig("com.crunchify.tutorial");
        // This tutorial required and then enable below line: http://crunchify.me/1VIwInK
        //crunchifyResourceConfig.getContainerResponseFilters().add(CrunchifyCORSFilter.class);
        return HttpServerFactory.create(getCrunchifyURI(), crunchifyResourceConfig);
    }
 
    private static URI getCrunchifyURI() {
        return UriBuilder.fromUri("http://" + crunchifyGetHostName() + "/").port(8085).build();
    }
 
    private static String crunchifyGetHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }
}
