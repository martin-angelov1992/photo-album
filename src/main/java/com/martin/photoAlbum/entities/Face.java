package com.martin.photoAlbum.entities;

import com.martin.photoAlbum.business.facerecognition.Rectangle;

public class Face {
	private Rectangle rectangle;
	private Account person;
	private Photo photo;

	public Face(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public Account getPerson() {
		return person;
	}

	public void setPerson(Account person) {
		this.person = person;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
}