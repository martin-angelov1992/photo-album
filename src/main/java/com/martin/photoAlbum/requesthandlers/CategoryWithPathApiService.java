package com.martin.photoAlbum.requesthandlers;

import com.martin.photoAlbum.business.CategoryService;

public class CategoryWithPathApiService extends ApiService<CategoryService> {

	private static final long serialVersionUID = 1L;

	public CategoryWithPathApiService() {
		super(new CategoryService());
	}
}