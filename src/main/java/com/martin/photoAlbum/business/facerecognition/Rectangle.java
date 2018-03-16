package com.martin.photoAlbum.business.facerecognition;

import javax.persistence.Column;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Rectangle {
	@Embedded
    @AttributeOverrides({
        @AttributeOverride(name="x",
                           column=@Column(name="x1")),
        @AttributeOverride(name="y",
                           column=@Column(name="y1"))
    })
	private Point pointA;
	@Embedded
    @AttributeOverrides({
        @AttributeOverride(name="x",
                           column=@Column(name="x2")),
        @AttributeOverride(name="y",
                           column=@Column(name="y2"))
    })
	private Point pointB;

	public Rectangle() {}

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

	public double getSize() {
		int length1 = pointB.getX() - pointA.getX();
		int length2 = pointB.getY() - pointA.getY();

		return length1*length2;
	}

	public List<Side> getSides() {
		List<Side> sides = new ArrayList<>(4);

		List<Point> otherPoints = getOhter2Points();
		List<Point> points = new ArrayList<>(4);
		points.add(pointA);
		points.add(otherPoints.get(0));
		points.add(pointB);
		points.add(otherPoints.get(1));

		for (int i=0;i<4;++i) {
			Point p1 = points.get(i);
			Point p2 = points.get((i+1)%4);
			sides.add(new Side(p1, p2));
		}

		return sides;
	}

	private List<Point> getOhter2Points() {
		List<Point> points = new ArrayList<>(2);

		points.add(new Point(pointA.getX(), pointB.getY()));
		points.add(new Point(pointB.getX(), pointA.getY()));

		return points;
	}

	public static class Side {
		private Point pointA;
		private Point pointB;

		private Side(Point pointA, Point pointB) {
			this.pointA = pointA;
			this.pointB = pointB;
		}

		public Point getPointA() {
			return pointA;
		}

		public Point getPointB() {
			return pointB;
		}

		public Point getIntersectionWith(Side side) {
	        // Line AB represented as a1x + b1y = c1
	        double a1 = pointB.getY() - pointA.getY();
	        double b1 = pointA.getX() - pointB.getX();
	        double c1 = a1*(pointA.getX()) + b1*(pointA.getY());
	      
	        // Line CD represented as a2x + b2y = c2
	        double a2 = side.pointB.getY() - side.pointA.getY();
	        double b2 = side.pointA.getX() - side.pointB.getX();
	        double c2 = a2*side.pointA.getX()+ b2*side.pointA.getY();
	      
	        double determinant = a1*b2 - a2*b1;
	      
	        if (determinant == 0)
	        {
	            // The lines are parallel.
	            return null;
	        }
	        else
	        {
	            int x = (int) Math.round((b2*c1 - b1*c2)/determinant);
	            int y = (int) Math.round((a1*c2 - a2*c1)/determinant);

	            if (isBetween(x, pointA.getX(), pointB.getX()) && 
	            		isBetween(x, side.pointA.getX(), side.pointB.getX()) && 
	            		isBetween(y, pointA.getY(), pointB.getY()) && 
	            		isBetween(y, side.pointA.getY(), side.pointB.getY())) {
	            	return new Point(x, y);
	            }

	            System.out.println(String.format("intersection point is: (%d,%d)", x, y));
	            System.out.println(String.format("%s line does not intersect with %s", this, side));
	            return null;
	        }
		}

		private boolean isBetween(int i, int i1, int i2) {
			return (i >= i1 && i <= i2) || (i >= i2 && i <= i1);
		}

		public String toString() {
			return String.format("(%d,%d)-(%d,%d)", pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
		}
	}

	public List<Point> getAllPoints() {
		List<Point> points = new ArrayList<>(4);

		points.add(pointA);
		points.add(new Point(pointA.getX(), pointB.getY()));
		points.add(pointB);
		points.add(new Point(pointB.getX(), pointA.getY()));

		return points;
	}
}