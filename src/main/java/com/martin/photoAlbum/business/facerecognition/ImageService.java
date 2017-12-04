package com.martin.photoAlbum.business.facerecognition;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.martin.photoAlbum.entities.Face;

public class ImageService {
	public BufferedImage getImage(byte[] bytes) {
		try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public BufferedImage rorate(BufferedImage image, int degree) {
		if (degree == 0) {
			return image;
		}

    	double angle = Math.toRadians(degree);
    	int newWidth = scale(angle, image.getWidth(), image.getHeight());
    	int newHeight = scale(angle, 
    			image.getHeight(), image.getWidth());
    	BufferedImage dstbimg = new 
                BufferedImage(newWidth, newHeight, image.getType());
        AffineTransform transform = new AffineTransform();
        transform.translate((newWidth - image.getWidth())/2, (newHeight - image.getHeight())/2);
        transform.rotate(angle, image.getWidth()/2, image.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, dstbimg);

        return dstbimg;
    }

	private static int scale(double angle, int a, int b) {
		return (int)Math.ceil(a*Math.abs(Math.cos(angle)) + b*Math.abs(Math.sin(angle)));
	}

	public Point getCenter(int angle, BufferedImage image) {
    	int newWidth = scale(angle, image.getWidth(), image.getHeight());
    	int newHeight = scale(angle, 
    			image.getHeight(), image.getWidth());

    	return new Point(newWidth/2, newHeight/2);
    }

	public BufferedImage getRect(BufferedImage image, Face face) {
		Rectangle rect = face.getRectangle();

		Point pointA = rect.getPointA();

		return image.getSubimage(pointA.getX(), pointA.getY(), rect.calcWidth(), rect.calcHeight());
	}
}