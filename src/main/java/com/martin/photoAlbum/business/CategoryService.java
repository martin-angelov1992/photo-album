package com.martin.photoAlbum.business;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.entities.Account;
import com.martin.photoAlbum.entities.Category;

public class CategoryService {

	public enum DeleteResult {
		NOT_OWNER, NOT_LOGGED_IN, OK

	}

	public enum EditResult {
		CATEGORY_NOT_FOUND, NOT_OWNER, NOT_LOGGED_IN, OK

	}

	private final static Logger logger = Logger.getLogger(CategoryService.class);
	private Session session;
	
	public CategoryService(Session session) {
		this.session = session;
	}
	
	public DeleteResult delete(int id) {
		if (!session.isLoggedIn()) {
			return DeleteResult.NOT_LOGGED_IN;
		}
		
		Category category = getById(id);
		Account account = session.getAccount();
		
		if (!isOwner(account, category)) {
			return DeleteResult.NOT_OWNER;
		}
		
		EntityManager em = Data.getInstance().getEntityManager();
		
		em.remove(category);
		
		return DeleteResult.OK;
	}
	
	public AddCategoryResult add(String name) {
		return add(name, null);
	}
	
	public AddCategoryResult add(String name, Integer parentId) {
		if (name == null || name.equals("")) {
			AddCategoryResult result = new AddCategoryResult();
			
			result.setErrorCode(ErrorCode.EMPTY_NAME);
			
			return result;
		}
		
		Integer newId;
		
		if (parentId == null) {
			newId = addParentCategory(name);
		} else {
			newId = addChildCategory(name, parentId);
		}
		
		AddCategoryResult result = new AddCategoryResult();
		
		result.setErrorCode(ErrorCode.OK);
		result.setNewId(newId);
		
		return result;
	}
	
	private Integer addChildCategory(String name, Integer parentId) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category parent = em.getReference(Category.class, parentId);
		
		if (parent == null) {
			logger.error("Unable to find parent category");
			return null;
		}
		
		Category category = new Category();
		
		em.persist(category);
		em.flush();
		
		return category.getId();
	}

	private Integer addParentCategory(String name) {
		Category category = new Category();
		
		category.setName(name);
		
		EntityManager em = Data.getInstance().getEntityManager();
		em.persist(category);
		em.flush();
		
		return category.getId();
	}

	public EditResult edit(int id, String newName) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category category = getById(id);
		
		if (category == null) {
			return EditResult.CATEGORY_NOT_FOUND;
		}
		
		if (!session.isLoggedIn()) {
			return EditResult.NOT_LOGGED_IN;
		}
		
		if (!isOwner(session.getAccount(), category)) {
			return EditResult.NOT_OWNER;
		}
		
		category.setName(newName);
		
		em.persist(category);
		
		return EditResult.OK;
	}
	
	private boolean isOwner(Account account, Category category) {
		// TODO Auto-generated method stub
		return false;
	}

	public static enum ErrorCode {
		OK, EMPTY_NAME
	}
	
	public static class AddCategoryResult {
		private Integer newId;
		private ErrorCode errorCode;

		public ErrorCode getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(ErrorCode errorCode) {
			this.errorCode = errorCode;
		}

		public Integer getNewId() {
			return newId;
		}

		public void setNewId(Integer newId) {
			this.newId = newId;
		}
	}

	public void renameParentCategory(String name, String newName) {
		EntityManager em = Data.getInstance().getEntityManager();
		Query q = em.createQuery(
				"SELECT c FROM Category c WHERE name = :name AND category.parent IS NULL");
		q.setParameter("name", name);
		
		Category category = (Category)q.getSingleResult();
		
		category.setName(newName);
		
		em.persist(category);
	}
	
	public Category getById(int id) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category category = em.find(Category.class, id);
		
		return category;
	}
}
