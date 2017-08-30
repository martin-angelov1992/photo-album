package com.martin.photoAlbum.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.entities.Account;
import com.martin.photoAlbum.entities.Category;
import com.martin.photoAlbum.entities.Item;
import com.martin.photoAlbum.entities.Photo;

import dto.PathDto;
import dto.PhotoDto;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

public class PhotoService extends Service {
	public PhotoService(Session session) {
		super(session);
	}

	private final static Logger logger = Logger.getLogger(CategoryService.class);
	private static final int THUMBNAIL_MAX_WIDTH = 500;
	private static final int THUMBNAIL_MAX_HEIGHT = 500;
	
	public enum DeleteResult {
		PHOTO_NOT_FOUND, NOT_LOGGED_IN, NOT_OWNER, OK

	}

	public enum EditResult {
		NOT_LOGGED_IN, PHOTO_NOT_FOUND, OK

	}

	public static class AddResult {
		public enum Status {NOT_LOGGED_IN, CATEGORY_DOES_NOT_EXIST, OK}
		
		private Status status;
		private int id;
		
		public AddResult(Status status) {
			this.status = status;
		}
		
		public void setId(int id) {
			this.id = id;
		}

		public Status getStatus() {
			return status;
		}

		public int getId() {
			return id;
		}		
	}
	
	public AddResult add(int categoryID, String name, String description, byte[] photoContent) {
		if (!session.isLoggedIn()) {
			return new AddResult(AddResult.Status.NOT_LOGGED_IN);
		}
		
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category category = em.find(Category.class, categoryID);
		
		if (category == null) {
			return new AddResult(AddResult.Status.CATEGORY_DOES_NOT_EXIST);
		}
		
		byte[] thumbnailBytes = new byte[0];//generateThumbnail(photoContent);
		
		Photo photo = new Photo();
		photo.setDateAdded(new Date());
		photo.setDescription(description);
		photo.setName(name);
		photo.setImage(photoContent);
		photo.setThumbnail(thumbnailBytes);
		
		em.persist(photo);
		
		category.addPhoto(photo);
		
		em.persist(category);
		
		AddResult result = new AddResult(AddResult.Status.OK);
		result.setId(photo.getId());
		
		return result;
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
		double heightRatio = THUMBNAIL_MAX_HEIGHT/height;
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

	public PhotoDto getDtoById(int id) {
		Photo photo = getById(id);
		return convert(photo);
	}
	
	public Photo getById(int id) {
		return Data.getInstance().getEntityManager().find(Photo.class, id);
	}

	private PhotoDto convert(Photo photo) {
		PhotoDto dto = new PhotoDto();
		
		dto.setId(photo.getId());
		dto.setName(photo.getName());
		dto.setDescription(photo.getDescription());
		dto.setDateAdded(photo.getDateAdded().toString());
		dto.setOwner(photo.getOwner().getName());
		dto.setPath(getPath(photo));
		
		return dto;
	}

	private PathDto[] getPath(Photo photo) {
		List<PathDto> path = new LinkedList<>();
		
		Category parent = getParent(photo);
		
		while(parent != null) {
			PathDto pathDto = new PathDto();
			pathDto.setId(parent.getId());
			pathDto.setName(parent.getName());
			
			path.add(pathDto);
			parent = getParent(parent);
		}
		
		return path.toArray(new PathDto[0]);
	}

	private Category getParent(Item item) {
		Query q = Data.getInstance().getEntityManager().createQuery(
				"SELECT c FROM Category c WHERE parent.id=:parentId");
			q.setParameter("parentId", item.getId());
			try {
				return (Category) q.getSingleResult();
			} catch (NoResultException exc) {
				return null;
			}
	}
}
