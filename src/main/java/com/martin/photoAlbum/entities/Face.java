package com.martin.photoAlbum.entities;

import java.util.List;

public class Face {
	private List<Integer> squarePoints;
	private Account person;

	public List<Integer> getSquarePoints() {
		return squarePoints;
	}

	public void setSquarePoints(List<Integer> squarePoints) {
		this.squarePoints = squarePoints;
	}

	public Account getPerson() {
		return person;
	}

	public void setPerson(Account person) {
		this.person = person;
	}
}