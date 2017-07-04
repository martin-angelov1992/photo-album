package dto;

import java.util.Collection;

public class SimpleCategory {
	private String name;
	private int id;
	
	private Collection<SimpleCategory> categories;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Collection<SimpleCategory> getCategories() {
		return categories;
	}

	public void setCategories(Collection<SimpleCategory> categories) {
		this.categories = categories;
	}
}