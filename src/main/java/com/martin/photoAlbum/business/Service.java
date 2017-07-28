package com.martin.photoAlbum.business;

import com.martin.photoAlbum.Session;

public class Service {
	protected Session session;
	
	public Service(Session session) {
		this.session = session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
}
