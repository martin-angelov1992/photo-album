package dto;

import java.util.List;

public class CategoryDto {
	private int id;
	private String owner;
	private String name;
	private List<CategoryDto> subCategories;
	private List<ThumbnailDto> thumbnails;
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CategoryDto> getSubCategories() {
		return subCategories;
	}
	
	public void setSubCategories(List<CategoryDto> subCategories) {
		this.subCategories = subCategories;
	}
	
	public List<ThumbnailDto> getThumbnails() {
		return thumbnails;
	}
	
	public void setThumbnails(List<ThumbnailDto> thumbnails) {
		this.thumbnails = thumbnails;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}