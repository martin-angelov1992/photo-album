package com.martin.photoAlbum.entities;

import java.util.HashSet;
import java.util.Set;

public class Category extends Item {
	private Set<Item> subItems;
	
	public Category() {
		subItems = new HashSet<>();
	}
	
	public Set<Item> getSubItems() {
		return subItems;
	}
	
	public void addSubCategry(Category category) {
		subItems.add(category);
	}
	
	public void addPhoto(Photo photo) {
		subItems.add(photo);
	}
}