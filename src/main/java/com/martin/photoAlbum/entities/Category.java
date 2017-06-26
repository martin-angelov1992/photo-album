package com.martin.photoAlbum.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Category extends Item {
	private @Id @GeneratedValue int id;
	private Set<Item> subItems;
	private String name;
	
	public Category() {
		subItems = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
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