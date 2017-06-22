package com.martin.photoAlbum;

import java.util.HashSet;
import java.util.Set;

public class RequestHandlers {
	private Set<Class<?>> requestHandlers;
	
	private RequestHandlers() {
		requestHandlers = new HashSet<>();
	}
	
	public Set<Class<?>> getAll() {
		return requestHandlers;
	}
}
