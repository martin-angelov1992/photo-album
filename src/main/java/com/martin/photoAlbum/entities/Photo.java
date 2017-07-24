package com.martin.photoAlbum.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Photo extends Item {
	private String description;
	private Date dateAdded;
	@Lob
	private byte[] image;
	@Lob
	private byte[] thumbnail;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
}
