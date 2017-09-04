package com.martin.photoAlbum.requesthandlers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.Service;
import com.martin.photoAlbum.entities.Account;

public class ApiService<T extends Service> extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Context
    protected HttpServletRequest webRequest;
	
	protected T service;
	
	public ApiService(T service) {
		this.service = service;
	}
	
	public Session getSession() {
		AccountService accService = new AccountService();
		Object accIdObj = webRequest.getSession().getAttribute("accId");
		
		if (accIdObj == null) {
			return new Session();
		}
		
		Account acc = accService.getById((Integer)accIdObj);
		
		return new Session(acc);
	}
	
	protected void updateSession() {
		service.setSession(getSession());
	}

	@Context
	public void setServletContext(ServletContext servletContext) {
		updateSession();
	}
}