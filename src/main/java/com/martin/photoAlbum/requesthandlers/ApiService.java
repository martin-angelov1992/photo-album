package com.martin.photoAlbum.requesthandlers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.Service;
import com.martin.photoAlbum.entities.Account;

public class ApiService<T extends Service> {
	@Context
    private HttpServletRequest webRequest;
	
	protected T service;
	
	public Session getSession() {
		AccountService accService = new AccountService(null);
		Object accIdObj = webRequest.getSession().getAttribute("accId");
		
		if (accIdObj == null) {
			return new Session();
		}
		
		Account acc = accService.getById(Integer.valueOf((String)accIdObj));
		
		
	}
	
	protected void updateSession() {
		service.setSession(getSession());
	}
}
