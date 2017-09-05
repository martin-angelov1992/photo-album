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
		EntityManager em = Data.getInstance().getEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			super.handle(baseRequest, request, response);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			return;
		}
		tx.commit();
	}
}