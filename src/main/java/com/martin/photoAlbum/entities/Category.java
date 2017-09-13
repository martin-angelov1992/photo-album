package com.martin.photoAlbum.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Category extends Item {
	@OneToMany(mappedBy="parent", cascade=CascadeType.REMOVE)
	private Set<Category> subCategories;
	@OneToMany(cascade=CascadeType.REMOVE)
	private Set<Photo> photos;
	
	public Category() {
		subCategories = new HashSet<>();
		photos = new HashSet<>();
	}
	
	public Set<Category> getSubCategories() {
		return subCategories;
	}
	public Set<Photo> getPhotos() {
		return photos;
	}

	public void addSubCategry(Category category) {
		subCategories.add(category);
	}
	
	public void addPhoto(Photo photo) {
		photos.add(photo);
	}
}