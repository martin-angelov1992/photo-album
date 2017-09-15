package dto;

public class ThumbnailDto {
	private int id;
	private String name;
	private Integer ownerID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Integer ownerID) {
		this.ownerID = ownerID;
	}
}
