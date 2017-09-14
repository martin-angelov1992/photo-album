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
import dto.ThumbnailDto;

public class CategoryService extends Service {

	public CategoryService() {}
	
	public enum DeleteResult {
		NOT_OWNER, NOT_LOGGED_IN, OK

	}

	public enum EditResult {
		CATEGORY_NOT_FOUND, NOT_OWNER, NOT_LOGGED_IN, OK

	}

	private final static Logger logger = Logger.getLogger(CategoryService.class);
	
	public CategoryService(Session session) {
		super(session);
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
			return new AddCategoryResult(ErrorCode.EMPTY_NAME);
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
		category.setName(name);
		category.setOwner(session.getAccount());
		category.setParent(parent);
		
		em.persist(category);
		em.flush();
		
		return category.getId();
	}

	private Integer addParentCategory(String name) {
		Category category = new Category();
		
		category.setName(name);
		category.setOwner(session.getAccount());
		
		EntityManager em = Data.getInstance().getEntityManager();
		em.persist(category);
		em.flush();
		
		return category.getId();
	}

	public EditResult edit(int id, String newName, Integer parentId) {
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

		if (parentId == null) {
			category.setParent(null);
		} else {
			category.setParent(getById(parentId));
		}

		category.setName(newName);
		
		em.persist(category);
		
		return EditResult.OK;
	}
	
	private boolean isOwner(Account account, Category category) {
		if (category.getOwner() == null) {
			return false;
		}
		
		return category.getOwner().equals(account);
	}

	public static enum ErrorCode {
		OK, EMPTY_NAME, ILLEGAL_CHARACTER
	}
	
	public static class AddCategoryResult {
		private Integer newId;
		private ErrorCode errorCode;

		public AddCategoryResult() {}
		
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
		
		Query q1 = em.createQuery(
				"SELECT c FROM Category c WHERE c.name = :name AND c.parent IS NULL and c.owner IS NOT NULL order by c.owner.id desc");
		q1.setParameter("name", name);
		
		System.out.println(q1.getResultList());
		System.out.println("name="+name);
		System.out.println("account="+session.getAccount().getId());
		System.out.println("categoryOwner="+((Category)q1.getResultList().get(0)).getOwner().getId());
		
		Query q = em.createQuery(
				"SELECT c FROM Category c WHERE c.name = :name AND c.parent IS NULL and c.owner IS NOT NULL "
				+ "and c.owner.id=:ownerId");
		q.setParameter("name", name);
		q.setParameter("ownerId", session.getAccount().getId());
		
		Category category = (Category)q.getResultList().get(0);
		
		category.setName(newName);
		
		em.persist(category);
	}
	
	public List<SimpleCategory> getCategoryTree() {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Query q = em.createQuery(
				"SELECT c FROM Category c WHERE c.parent IS NULL", Category.class);
		List<Category> categories = q.getResultList();
		
		List<SimpleCategory> simpleCategories = convertToSimpleCategories(categories);
		
		fillChildren(simpleCategories);
		
		return simpleCategories;
	}
	
	private void fillChildren(List<SimpleCategory> simpleCategories) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		for (SimpleCategory simpleCategory : simpleCategories) {
			Query q = em.createQuery("SELECT c FROM Category c WHERE c.parent.id=:parentId", Category.class);
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
		
		if (category.getOwner() != null) {
			dto.setOwner(category.getOwner().getName());
			dto.setOwnerId(category.getOwner().getId());
		}

		dto.setId(id);
		
		fillSubCategories(dto, id);
		fillThumbnails(dto, id);
		
		return dto;
	}
	
	private void fillThumbnails(CategoryDto dto, int categoryId) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Query q = em.createQuery("SELECT p FROM Photo p WHERE p.parent.id=:parentId", Photo.class);
		q.setParameter("parentId", categoryId);

		List<Photo> photos = q.getResultList();

		for (Photo photo : photos) {
			dto.getThumbnails().add(createThumbnailDto(photo));
		}
	}

	private ThumbnailDto createThumbnailDto(Photo photo) {
		ThumbnailDto dto = new ThumbnailDto();
		
		dto.setId(photo.getId());
		dto.setName(photo.getName());
		dto.setOwnerID(photo.getOwner().getId());

		return dto;
	}

	private void fillSubCategories(CategoryDto dto, int categoryId) {
		List<CategoryDto> subcategories = new LinkedList<>();
		EntityManager em = Data.getInstance().getEntityManager();
		
		Query q = em.createQuery("SELECT c FROM Category c WHERE c.parent.id=:parentId", Category.class);
		q.setParameter("parentId", categoryId);
		
		List<Category> categories = q.getResultList();
		
		for (Category category : categories) {
			CategoryDto subDto = new CategoryDto();
			subDto.setName(category.getName());

			if (category.getOwner() != null) {
				subDto.setOwner(category.getOwner().getName());
			}
			
			subDto.setId(category.getId());
			subcategories.add(subDto);
			fillSubCategories(subDto, category.getId());
		}
		
		dto.setSubCategories(subcategories);
	}

	public Category getById(int id) {
		EntityManager em = Data.getInstance().getEntityManager();
		
		Category category = em.find(Category.class, id);
		
		return category;
	}

	public CategoryWithPathDto getCategoryWithPath(int id) {
		CategoryWithPathDto dto = new CategoryWithPathDto();
		Category category = getById(id);

		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setPath(getPath(category));

		if (category.getParent() != null) {
			dto.setParentId(category.getParent().getId());
		}

		return dto;
	}

	public List<CategoryWithPathDto> getAll() {
		EntityManager em = Data.getInstance().getEntityManager();
		Query q = em.createQuery("SELECT c FROM Category c", Category.class);
		
		List<Category> categories = q.getResultList();
		
		List<CategoryWithPathDto> dtos = new ArrayList<>(categories.size());
		
		for (Category category : categories) {
			CategoryWithPathDto dto = new CategoryWithPathDto();
			
			dto.setId(category.getId());
			dto.setName(category.getName());
			dto.setPath(getPath(category));

			if (category.getParent() != null) {
				dto.setParentId(category.getParent().getId());
			}

			dtos.add(dto);
		}
		
		return dtos;
	}

	private String getPath(Category category) {
		List<String> parentNames = new LinkedList<>();
		Category parent = category.getParent();
		
		while (parent != null) {
			parentNames.add(parent.getName());
			parent = parent.getParent();
		}
		
		parentNames.add(category.getName());
	
		return String.join("\\", parentNames);
	}
}
