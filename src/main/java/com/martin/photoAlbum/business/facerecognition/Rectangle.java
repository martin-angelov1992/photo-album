package com.martin.photoAlbum.business.facerecognition;

public class Rectangle {
	private Point pointA;
	private Point pointB;

	public Rectangle(Point pointA, Point pointB) {
		super();
		this.pointA = pointA;
		this.pointB = pointB;
	}

	public Point getPointA() {
		return pointA;
	}
	public void setPointA(Point pointA) {
		this.pointA = pointA;
	}
	public Point getPointB() {
		return pointB;
	}
	public void setPointB(Point pointB) {
		this.pointB = pointB;
	}
	public int calcWidth() {
		return pointB.getX() - pointA.getX();
	}
	public int calcHeight() {
		return pointB.getY() - pointA.getY();
	}
}