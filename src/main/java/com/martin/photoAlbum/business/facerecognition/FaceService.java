package com.martin.photoAlbum.business.facerecognition;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.martin.photoAlbum.entities.Face;
import com.martin.photoAlbum.entities.Photo;

public class FaceService {
	private ImageService imageService;

	private static final int DEGREE_STEP = 10;
	private static final int MAX_DEGREES = 360;

	public void handleFaces(Photo photo) {
		List<Face> faces = getFaces(photo);
		assignPeople(faces);
	}

	private void assignPeople(List<Face> faces) {
		for (Face face : faces) {
			tryAssignPerson(face);
		}
	}

	private void tryAssignPerson(Face face) {
		BufferedImage image = imageService.getImage(face.getPhoto().getImage());
		BufferedImage faceImage = imageService.getRect(image, face);
		imageService.
	}

	private List<Face> getFaces(Photo photo) {
		List<Face> faces = new LinkedList<>();

		BufferedImage image = imageService.getImage(photo.getImage());
		image = getGreyVersion(image);
		for (int i=0;i<MAX_DEGREES;i+=DEGREE_STEP) {
			BufferedImage rotatedImage = imageService.rorate(image, i);

			List<Face> newFaces = getFaces(rotatedImage);

			removeFaces(image, i, newFaces);
			faces.addAll(newFaces);
		}

		return faces;
	}

	private void removeFaces(BufferedImage image, int degrees, List<Face> faces) {
		List<Face> rotated = getRotated(image, faces, degrees);

		for (Face face : rotated) {
			removeFace(image, face);
		}
	}

	private void removeFace(BufferedImage image, Face face) {
		Rectangle rect = face.getRectangle();

		Point pointA = rect.getPointA();

		image.getGraphics().drawRect(pointA.getX(), pointA.getY(), rect.calcWidth(), rect.calcHeight());
	}

	private List<Face> getRotated(BufferedImage image, List<Face> faces, int angle) {
		List<Face> rotatedFaces = new ArrayList<>(faces.size());

		for (Face face : faces) {
			Rectangle rectangle = face.getRectangle();
			Point pointA = rectangle.getPointA();
			Point pointB = rectangle.getPointB();

			Point center = imageService.getCenter(angle, image);

			Point rotatedPointA = getRotated(center.getX(), center.getY(), angle, pointA);
			Point rotatedPointB = getRotated(center.getY(), center.getY(), angle, pointB);

			Face newFace = new Face(new Rectangle(rotatedPointA, rotatedPointB));

			rotatedFaces.add(newFace);
		}

		return rotatedFaces;
	}

	private Point getRotated(int cx, int cy, int angle, Point p) {
	  double s = Math.sin(Math.toRadians(angle));
	  double c = Math.cos(Math.toRadians(angle));

	  int tmpX = p.getX();
	  int tmpY = p.getY();

	  // translate point back to origin:
	  tmpX -= cx;
	  tmpY -= cy;

	  // rotate point
	  int newX = (int)(tmpX * c - tmpY * s);
	  int newY = (int)(tmpX * s + tmpY * c);

	  // translate point back:
	  newX += cx;
	  newY += cy;

	  return new Point(newX, newY);
	}

	private List<Face> getFaces(BufferedImage rotatedImage) {
		// TODO Auto-generated method stub
		return null;
	}

	private BufferedImage getGreyVersion(BufferedImage image) {
		// TODO Auto-generated method stub
		return null;
	}
}