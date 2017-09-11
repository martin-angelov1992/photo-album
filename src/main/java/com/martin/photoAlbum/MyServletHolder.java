package com.martin.photoAlbum;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class MyServletHolder extends ServletHolder {

	public MyServletHolder(ServletContainer servletContainer) {
		super(servletContainer);
	}

	@Override
	public void handle(Request baseRequest, ServletRequest request, ServletResponse response)
			throws ServletException, UnavailableException, IOException {
		Data.getInstance().createNewEM();
		
		EntityManager em = Data.getInstance().getEntityManager();

		EntityTransaction tx = em.getTransaction();

		System.out.println("Starting transaction.");
		tx.begin();
		try {
			super.handle(baseRequest, request, response);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			return;
		}
		System.out.println("Ending transaction");
		tx.commit();
	}
}