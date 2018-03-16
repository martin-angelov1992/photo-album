package com.martin.photoAlbum.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.martin.photoAlbum.business.facerecognition.Point;
import com.martin.photoAlbum.business.facerecognition.Rectangle;

@Entity
public class Face {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) int id;
	@Embedded
	private Rectangle rectangle;
	@ManyToOne
	private Account person;
	@ManyToOne
	private Photo photo;

	public Face() {}

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}