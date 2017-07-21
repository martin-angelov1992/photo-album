package com.martin.photoAlbum.business;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.entities.Account;
import com.martin.photoAlbum.entities.Category;
import com.martin.photoAlbum.entities.Photo;

import dto.CategoryDto;
import dto.CategoryWithPathDto;
import dto.SimpleCategory;

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
			return new AddCategoryResult(ErrorCode.EMPTY_NAME);;
		}
		
		if (name.contains("/")) {
			return new AddCategoryResult(ErrorCode.ILLEGAL_CHARACTER);
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
		OK, EMPTY_NAME, ILLEGAL_CHARACTER
	}
	
	public static class AddCategoryResult {
		private Integer newId;
		private ErrorCode errorCode;

		public AddCategoryResult(ErrorCode errorCode) {
			this.errorCode = errorCode;
		}

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
				"SELECT c FROM Category c WHERE name = :name AND c.parent IS NULL");
		q.setParameter("name", name);
		
		Category category = (Category)q.getSingleResult();
		
		category.setName(newName);
		
		em.persist(category);
	}
	
	public List<SimpleCategory> getCategoryTree() {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Query q = em.createQuery(
				"SELECT c FROM Category c c.parent IS NULL", Category.class);
		List<Category> categories = q.getResultList();
		
		List<SimpleCategory> simpleCategories = convertToSimpleCategories(categories);
		
		fillChildren(simpleCategories);
		
		return simpleCategories;
	}
	
	private void fillChildren(List<SimpleCategory> simpleCategories) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		for (SimpleCategory simpleCategory : simpleCategories) {
			Query q = em.createQuery("SELECT c FROM Category c c.parent.id=:parentId", Category.class);
			q.setParameter("parentId", simpleCategory.getId());
			
			List<Category> categories = q.getResultList();
			
			if (categories.size() == 0) {
				continue;
			}
			
			List<SimpleCategory> childCategories = convertToSimpleCategories(categories);
			
			simpleCategory.setCategories(childCategories);
			
			fillChildren(childCategories);
		}
	}

	private List<SimpleCategory> convertToSimpleCategories(List<Category> categories) {
		List<SimpleCategory> simpleCategories = new ArrayList<>(categories.size());

		for (Category category : categories) {
			simpleCategories.add(convertToSimpleCategory(category));
		}
		
		return simpleCategories;
	}

	private SimpleCategory convertToSimpleCategory(Category category) {
		SimpleCategory simpleCategory = new SimpleCategory();
		
		simpleCategory.setId(category.getId());
		simpleCategory.setName(category.getName());
		
		return simpleCategory;
	}

	public CategoryDto getDtoById(int id) {
		CategoryDto dto = new CategoryDto();
		
		Category category = getById(id);
		
		if (category == null) {
			return null;
		}
		
		dto.setName(category.getName());
		dto.setOwner(category.getOwner().getName());
		dto.setId(id);
		
		fillSubCategories(dto, id);
		fillThumbnails(dto, id);
		
		return dto;
	}
	
	private void fillThumbnails(CategoryDto dto, int categoryId) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Query q = em.createQuery("SELECT p FROM Photo p p.parent.id=:parentId", Photo.class);
		q.setParameter("parentId", categoryId);
	}

	private void fillSubCategories(CategoryDto dto, int categoryId) {
		List<CategoryDto> subcategories = new LinkedList<>();
		EntityManager em = Data.getInstance().getEntityManager();
		
		Query q = em.createQuery("SELECT c FROM Category c c.parent.id=:parentId", Category.class);
		q.setParameter("parentId", categoryId);
		
		List<Category> categories = q.getResultList();
		
		for (Category category : categories) {
			CategoryDto subDto = new CategoryDto();
			subDto.setName(category.getName());
			subDto.setOwner(category.getOwner().getName());
			subDto.setId(category.getId());
			fillSubCategories(subDto, category.getId());
		}
		
		dto.setSubCategories(subcategories);
	}

	public Category getById(int id) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category category = em.find(Category.class, id);
		
		return category;
	}
	
	public List<CategoryWithPathDto> getAll() {
		EntityManager em = Data.getInstance().getEntityManager();
		Query q = em.createQuery("SELECT c FROM Category c", Category.class);
		
		List<Category> categories = q.getResultList();
		
		List<CategoryWithPathDto> dtos = new ArrayList<>(categories.size());
		
		for (Category category : categories) {
			CategoryWithPathDto dto = new CategoryWithPathDto();
			
			dto.setId(category.getId());
			dto.setName(dto.getName());
			dto.setPath(getPath(category));
			
			dtos.add(dto);
		}
		
		return dtos;
	}

	private String getPath(Category category) {
		List<String> parentNames = new LinkedList<>();
		Category parent;
		do {
			parent = getParent(category);
			parentNames.add(parent.getName());
		} while (parent != null);
		return null;
	}

	private Category getParent(Category category) {
		// TODO Auto-generated method stub
		return null;
	}
}
