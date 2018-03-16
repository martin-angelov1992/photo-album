package com.martin.photoAlbum;

import com.martin.photoAlbum.business.facerecognition.FaceService;
import com.martin.photoAlbum.business.facerecognition.Point;
import com.martin.photoAlbum.business.facerecognition.Rectangle;
import com.martin.photoAlbum.entities.Face;

import junit.framework.Assert;
import junit.framework.TestCase;

public class FaceServiceTest extends TestCase {
	private FaceService service = new FaceService();

	public void testIsSame() {
		Face bigFace = new Face();
		bigFace.setRectangle(new Rectangle(new Point(55, 30), new Point(200, 265)));

		Face innerFace = new Face();
		innerFace.setRectangle(new Rectangle(new Point(100, 65), new Point(135, 90)));

		Face outerFace = new Face();
		outerFace.setRectangle(new Rectangle(new Point(220, 145), new Point(260, 170)));

		Face sameFace1 = new Face();
		sameFace1.setRectangle(new Rectangle(new Point(150, 40), new Point(210, 100)));

		Face sameFace2 = new Face();
		sameFace2.setRectangle(new Rectangle(new Point(150, 120), new Point(210, 170)));

		Face lowerRightFace = new Face();
		lowerRightFace.setRectangle(new Rectangle(new Point(176, 142), new Point(213, 195)));

		Face rightFace = new Face();
		rightFace.setRectangle(new Rectangle(new Point(185, 50), new Point(209, 215)));

		Assert.assertTrue(service.isSame(bigFace, innerFace));
		Assert.assertFalse(service.isSame(bigFace, outerFace));
		Assert.assertTrue(service.isSame(bigFace, sameFace1));
		Assert.assertTrue(service.isSame(bigFace, sameFace2));
		Assert.assertFalse(service.isSame(bigFace, lowerRightFace));
		Assert.assertFalse(service.isSame(bigFace, rightFace));
	}
}