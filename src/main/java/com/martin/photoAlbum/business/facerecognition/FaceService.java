package com.martin.photoAlbum.business.facerecognition;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.persistence.Query;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.facerecognition.Rectangle.Side;
import com.martin.photoAlbum.entities.Account;
import com.martin.photoAlbum.entities.Face;
import com.martin.photoAlbum.entities.Photo;

public class FaceService {
	private ImageService imageService;
	private OpenCVService openCVService;
	private AccountService accountService;

	private static final int DEGREE_STEP = 10;
	private static final int MAX_DEGREES = 360;

	// Indicates how many pictures of a person we should have
	// in order to trust our prediction system, when it predicts that the
	// owner of the photo is a particular user
	private static final int APPEARANCES_THRESHOLD = 4;

	public FaceService() {
		imageService = new ImageService();
		openCVService = OpenCVService.getInstance();
	}

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

		faceImage = getGreyVersion(faceImage);
		try {
			ImageIO.write(faceImage, "jpg", new File("c:\\test\\found\\image"+
					ThreadLocalRandom.current().nextInt()+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Integer prediction = openCVService.predict(faceImage);

		if (prediction == null) {
			return;
		}

		Account predictionAcc = accountService.getById(prediction);

		if (predictionAcc == null) {
			return;
		}

		int appearances = getAppearances(predictionAcc);

		if (appearances < APPEARANCES_THRESHOLD) {
			return;
		}

		face.setPerson(predictionAcc);
	}

	private int getAppearances(Account predictionAcc) {
		Query q = Data.getInstance().getEntityManager()
				.createQuery("SELECT COUNT(f.id) FROM Face f WHERE f.account.id=:id");
		q.setParameter("id", predictionAcc.getId());

		int count = (int)q.getSingleResult();

		return count;
	}

	private List<Face> getFaces(Photo photo) {
		List<Face> faces = new LinkedList<>();

		BufferedImage image = imageService.getImage(photo.getImage());
		image = getGreyVersion(image);
		for (int i=0;i<MAX_DEGREES;i+=DEGREE_STEP) {
			BufferedImage rotatedImage = imageService.rorate(image, i);

			List<Face> newFaces = getFaces(rotatedImage, photo);

			newFaces = filterRepeatingFaces(newFaces);
			removeFaces(image, i, newFaces);
			faces.addAll(newFaces);

			try {
				ImageIO.write(image, "jpg", new File("c:\\test\\removedFaces"+
						ThreadLocalRandom.current().nextInt()+".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return faces;
	}

	private List<Face> filterRepeatingFaces(List<Face> faces) {
		List<Face> newFaces = new LinkedList<>();

		for (Face face : faces) {
			if (!contains(newFaces, face)) {
				newFaces.add(face);
			}
		}

		return newFaces;
	}

	private boolean contains(List<Face> faces, Face face) {
		for (Face oldFace : faces) {
			if (isSame(oldFace, face)) {
				return true;
			}
		}

		return false;
	}

	public boolean isSame(Face oldFace, Face face) {
		boolean oldBigger = isBigger(oldFace, face);
		Face bigger = oldBigger ? oldFace : face;
		Face smaller = oldBigger ? face : oldFace;

		if (contains(bigger, smaller)) {
			return true;
		}

		Rectangle intersection = getIntersection(bigger, smaller);

		if (intersection == null) {
			return false;
		}

		double intersectionSize = intersection.getSize();
		double biggerFaceSize = bigger.getRectangle().getSize();

		return intersectionSize*2 > biggerFaceSize;
	}

	private Rectangle getIntersection(Face bigger, Face smaller) {
		List<Point> innerPoints = getInnerPoints(bigger, smaller);
		List<Point> intersectionPoints = getIntersectionPoints(bigger, smaller);
		List<Point> allPoints = new ArrayList<>();
		allPoints.addAll(innerPoints);
		allPoints.addAll(intersectionPoints);

		if (allPoints.size() == 3) {
			allPoints = find4PointsFrom3(allPoints);
		}

		if (allPoints.size() == 0) {
			return null;
		}

		return findRectangleByPoints(allPoints);
	}

	private Rectangle findRectangleByPoints(List<Point> points) {
		Point p1 = points.get(0);
		int smallerY = p1.getY();
		int biggerY = p1.getY();
		int smallerX = p1.getX();
		int biggerX = p1.getX();

		for (int i=1;i<points.size();++i) {
			Point point = points.get(i);

			if (point.getX() < smallerX) {
				smallerX = point.getX();
			}

			if (point.getX() > biggerX) {
				biggerX = point.getX();
			}

			if (point.getY() < smallerY) {
				smallerY = point.getY();
			}

			if (point.getY() > biggerY) {
				biggerY = point.getY();
			}
		}

		return new Rectangle(new Point(smallerX, smallerY), new Point(biggerX, biggerY));
	}

	private List<Point> find4PointsFrom3(List<Point> points) {
		for (int i=0;i<points.size();++i) {
			Point point1 = points.get(i);

			for (int j=i+1;j<points.size();++j) {
				Point point2 = points.get(j);

				if (point1.equals(point2)) {
					int indexLeft;
					if (i == 0 && j == 1) {
						indexLeft = 2;
					} else if (i == 0 && j == 3) {
						indexLeft = 1;
					} else {
						indexLeft = 3;
					}

					List<Point> allPoints = new ArrayList<>(points);
					allPoints.add(points.get(indexLeft).clone());

					return allPoints;
				}
			}
		}

		List<Point> oppositePoints = getOppositePoints(points);
		List<Point> otherPoints = getOtherPointsFromOposite(oppositePoints);
		List<Point> allPoints = new ArrayList<>(4);

		allPoints.addAll(oppositePoints);
		allPoints.addAll(otherPoints);

		return allPoints;
	}

	private List<Point> getOtherPointsFromOposite(List<Point> oppositePoints) {
		List<Point> otherPoints = new ArrayList<>(2);

		Point p1 = oppositePoints.get(0);
		Point p2 = oppositePoints.get(1);

		otherPoints.add(new Point(p1.getX(), p2.getY()));
		otherPoints.add(new Point(p2.getX(), p1.getY()));

		return otherPoints;
	}

	private List<Point> getOppositePoints(List<Point> points) {
		List<Point> oppositePoints = new ArrayList<>(2);

		for (int i=0;i<points.size();++i) {
			Point point1 = points.get(i);
			for (int j=i+1;j<points.size();++j) {
				Point point2 = points.get(j);

				if (point1.getX() != point2.getX() && point1.getY() != point2.getY()) {
					oppositePoints.add(point1);
					oppositePoints.add(point2);
					return oppositePoints;
				}
			}
		}

		return oppositePoints;
	}

	private List<Point> getIntersectionPoints(Face bigger, Face smaller) {
		List<Point> intersectionPoints = new LinkedList<>();

		List<Side> biggerSides = bigger.getRectangle().getSides();
		List<Side> smallerSides = smaller.getRectangle().getSides();

		for (Side biggerSide : biggerSides) {
			for (Side smallerSide : smallerSides) {
				Point intersection = biggerSide.getIntersectionWith(smallerSide);

				if (intersection != null) {
					intersectionPoints.add(intersection);
				}
			}
		}

		return intersectionPoints;
	}

	private List<Point> getInnerPoints(Face bigger, Face smaller) {
		List<Point> points = new LinkedList<>();

		List<Point> biggerPoints = bigger.getRectangle().getAllPoints();
		List<Point> smallerPoints = smaller.getRectangle().getAllPoints();

		for (Point smallerPoint : smallerPoints) {
			if (contains(smallerPoint, biggerPoints)) {
				points.add(smallerPoint);
			}
		}

		return points;
	}

	private boolean contains(Point point, List<Point> polygon) {
		int i;
		int j;
		boolean result = false;
		for (i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
			Point iPoint = polygon.get(i);
			Point jPoint = polygon.get(j);
			if ((iPoint.getY() > point.getY()) != 
					(jPoint.getY() > point.getY()) &&
					(point.getX() < (jPoint.getX() - iPoint.getX()) * 
					(point.getY() - iPoint.getY()) / 
					(jPoint.getY()-iPoint.getY()) + iPoint.getX())) {
				result = !result;
			}
		}

		String polygonStr = polygonToString(polygon);

		System.out.println(String.format("%s point is not in %s polygon", point, polygonStr));
		
		return result;
	}

	private String polygonToString(List<Point> polygon) {
		List<String> pointStrings = new ArrayList<>(polygon.size());

		for (Point point : polygon) {
			pointStrings.add(point.toString());
		}

		return String.join(", ", pointStrings);
	}

	private boolean contains(Face bigger, Face smaller) {
		return getInnerPoints(bigger, smaller).size() == 4;
	}

	private boolean isBigger(Face face1, Face face2) {
		return face1.getRectangle().getSize() > face2.getRectangle().getSize();
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

		image.getGraphics().fillRect(pointA.getX(), pointA.getY(), rect.calcWidth(), rect.calcHeight());
	}

	private List<Face> getRotated(BufferedImage image, List<Face> faces, int angle) {
		List<Face> rotatedFaces = new ArrayList<>(faces.size());

		for (Face face : faces) {
			Rectangle rectangle = face.getRectangle();
			Point pointA = rectangle.getPointA();
			Point pointB = rectangle.getPointB();

			List<Point> points = findOtherRectanglePoints(pointA, pointB);
			points.add(pointA);
			points.add(pointB);

			List<Point> revertedPoints = new ArrayList<>(4);
			Point center = imageService.getCenter(angle, image);

			for (Point point : points) {
				Point revertedPoint = getRotated(center.getX(), center.getY(), -angle, point);
				revertedPoints.add(revertedPoint);
			}

			Rectangle faceRectangle = findWrappingRectangle(revertedPoints);

			Face newFace = new Face(faceRectangle);

			rotatedFaces.add(newFace);
		}

		return rotatedFaces;
	}

	private Rectangle findWrappingRectangle(List<Point> points) {
		List<Point> xSorted = new ArrayList<>(points);
		xSorted.sort((p1, p2) -> p1.getX()-p2.getX());
		List<Point> ySorted = new ArrayList<>(points);
		ySorted.sort((p1, p2) -> p1.getY()-p2.getY());
		int smallestY = ySorted.get(0).getY();
		int biggestY = ySorted.get(ySorted.size()-1).getY();
		int smallestX = xSorted.get(0).getX();
		int biggestX = xSorted.get(xSorted.size()-1).getX();

		return new Rectangle(new Point(smallestX, smallestY), 
				new Point(biggestX, biggestY));
	}

	private List<Point> findOtherRectanglePoints(Point pointA, Point pointB) {
		List<Point> otherPoints = new ArrayList<>(2);

		int xD = pointA.getX() > pointB.getX() ? pointA.getX() : pointB.getX();
		int yD = pointA.getY();

		int xC = pointA.getX() > pointB.getX() ? pointB.getX() : pointA.getX();
		int yC = pointA.getY();

		Point pointD = new Point(xD, yD);
		Point pointC = new Point(xC, yC);

		otherPoints.add(pointD);
		otherPoints.add(pointC);

		return otherPoints;
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

	private List<Face> getFaces(BufferedImage rotatedImage, Photo photo) {
		List<Rectangle> rectangles = openCVService.getFaces(rotatedImage);
		List<Face> faces = new ArrayList<>(rectangles.size());

		for (Rectangle rectangle : rectangles) {
			Face face = new Face(rectangle);

			face.setPhoto(photo);
			faces.add(face);
		}

		return faces;
	}

	private BufferedImage getGreyVersion(BufferedImage image) {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
		ColorConvertOp op = new ColorConvertOp(cs, null);  
		return op.filter(image, null);
	}

	public Face getByID(int id) {
		Query q = Data.getInstance().getEntityManager()
				.createQuery("SELECT f FROM Face f WHERE f.id=:id");
		q.setParameter("id", id);

		return (Face)q.getSingleResult();
	}
}