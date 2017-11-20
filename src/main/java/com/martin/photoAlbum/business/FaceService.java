package com.martin.photoAlbum.business;

import java.awt.image.BufferedImage;

import com.martin.photoAlbum.entities.Photo;

public class FaceService {

	private static final int DEGREE_STEP = 10;
	private static final int MAX_DEGREES = 360;

	public void handleFaces(Photo photo) {
		BufferedImage image = getImage(photo.getImage());
		for (int i=0;i<MAX_DEGREES;i+=DEGREE_STEP) {
			BufferedImage rotatedImage = rorate(image, i);
		}
	}

	private BufferedImage rorate(BufferedImage image, int degree) {
		if (degree == 0) {
			return image;
		}

		return null;
	}

	private BufferedImage getImage(byte[] image) {
		// TODO Auto-generated method stub
		return null;
	}
}