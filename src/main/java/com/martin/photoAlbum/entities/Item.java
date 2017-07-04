package com.martin.photoAlbum.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Item {
	private @Id @GeneratedValue int id;
	private Account owner;
	private String name;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
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
}
