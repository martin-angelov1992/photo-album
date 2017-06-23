package com.martin.photoAlbum;

import java.util.HashSet;
import java.util.Set;

import com.martin.photoAlbum.requesthandlers.CategoryApiService;
import com.martin.photoAlbum.requesthandlers.EditProfileApiService;
import com.martin.photoAlbum.requesthandlers.LoginApiService;
import com.martin.photoAlbum.requesthandlers.LogoutApiService;
import com.martin.photoAlbum.requesthandlers.PhotoApiService;
import com.martin.photoAlbum.requesthandlers.RegisterApiService;

public class RequestHandlers {
	private static volatile RequestHandlers instance;
	
	private Set<Class<?>> requestHandlers;
	
	private RequestHandlers() {
		requestHandlers = new HashSet<>();
		requestHandlers.add(LoginApiService.class);
		requestHandlers.add(RegisterApiService.class);
		requestHandlers.add(LoginApiService.class);
		requestHandlers.add(LogoutApiService.class);
		requestHandlers.add(EditProfileApiService.class);
		requestHandlers.add(CategoryApiService.class);
		requestHandlers.add(PhotoApiService.class);
	}
	
	public Set<Class<?>> getAll() {
		return requestHandlers;
	}
	
	public static RequestHandlers getInstance() {
		if (instance == null) {
			synchronized (RequestHandlers.class) {
				if (instance == null) {
					instance = new RequestHandlers();
				}
			}
		}
		
		return instance;
	}
}
