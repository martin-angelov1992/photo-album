package com.martin.photoAlbum.business;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.entities.Account;
import com.martin.photoAlbum.entities.Category;
import com.martin.photoAlbum.entities.Photo;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

public class PhotoService {
	private final static Logger logger = Logger.getLogger(CategoryService.class);
	private static final int THUMBNAIL_MAX_WIDTH = 500;
	private static final int THUMBNAIL_MAX_HEIGHT = 500;
	
	public enum DeleteResult {
		PHOTO_NOT_FOUND, NOT_LOGGED_IN, NOT_OWNER, OK

	}

	public enum EditResult {
		NOT_LOGGED_IN, PHOTO_NOT_FOUND, OK

	}

	public enum AddResult {
		NOT_LOGGED_IN, CATEGORY_DOES_NOT_EXIST, OK
	}

	private Session session;
	
	public PhotoService(Session session) {
		this.session = session;
	}
	
	public AddResult add(int categoryID, String name, String description, byte[] photoContent) {
		if (!session.isLoggedIn()) {
			return AddResult.NOT_LOGGED_IN;
		}
		
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category category = em.find(Category.class, categoryID);
		
		if (category == null) {
			return AddResult.CATEGORY_DOES_NOT_EXIST;
		}
		
		byte[] thumbnailBytes = generateThumbnail(photoContent);
		
		Photo photo = new Photo();
		photo.setDateAdded(new Date());
		photo.setDescription(description);
		photo.setName(name);
		photo.setImage(photoContent);
		photo.setThumbnail(thumbnailBytes);
		
		em.persist(photo);
		
		category.addPhoto(photo);
		
		em.persist(category);
		
		return AddResult.OK;
	}
	
	private byte[] generateThumbnail(byte[] photoContent) {
		try {
			MagickImage image = new MagickImage(new ImageInfo(), photoContent);
			
			ResizeInfo resizeInfo;
				resizeInfo = getResizeInfo(image.getDimension().getHeight(), image.getDimension().getWidth());
			
			MagickImage thumbnail = image.scaleImage(resizeInfo.width, resizeInfo.height);
			return thumbnail.imageToBlob(new ImageInfo());
		} catch (MagickException e) {
			logger.error("Unable to create thumbnail.");
			e.printStackTrace();
			return null;
		}
	}
	
	private ResizeInfo getResizeInfo(double height, double width) {
		double heightRatio = THUMBNAIL_MAX_WIDTH/height;
		double widthRatio = THUMBNAIL_MAX_WIDTH/width;
		
		double scaleRatio = widthRatio < heightRatio ? widthRatio : heightRatio;
		
		return new ResizeInfo((int)Math.floor(scaleRatio*width), (int)Math.floor(scaleRatio*height));
	}

	public static class ResizeInfo {
		private int width;
		private int height;
		
		public ResizeInfo(int width, int height) {
			this.height = height;
			this.width = width;
		}
	}

	public EditResult edit(int id, String name, String description) {
		if (!session.isLoggedIn()) {
			return EditResult.NOT_LOGGED_IN;
		}
		
		Photo photo = getById(id);
		
		if (photo == null) {
			return EditResult.PHOTO_NOT_FOUND;
		}
		
		photo.setName(name);
		photo.setDescription(description);
		
		EntityManager em = Data.getInstance().getEntityManager();
		
		em.persist(photo);
		
		return EditResult.OK;
	}
	
	public DeleteResult delete(int id) {
		if (!session.isLoggedIn()) {
			return DeleteResult.NOT_LOGGED_IN;
		}
		
		Photo photo = getById(id);
		
		if (photo == null) {
			return DeleteResult.PHOTO_NOT_FOUND;
		}
		
		Account account = session.getAccount();
		
		if (!isOowner(account, photo)) {
			return DeleteResult.NOT_OWNER;
		}
		
		Data.getInstance().getEntityManager().remove(photo);
		
		return DeleteResult.OK;
	}
	
	private boolean isOowner(Account account, Photo photo) {
		return photo.getOwner().equals(account);
	}

	public Photo getById(int id) {
		return Data.getInstance().getEntityManager().find(Photo.class, id);
	}
}
