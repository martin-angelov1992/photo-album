package com.martin.photoAlbum.entities;

public class Account {
	private String username;
	private String passHash;
	private String mail;
	private String name;
	
	public Account(String username, String passHash, String mail, String name) {
		super();
		this.username = username;
		this.passHash = passHash;
		this.mail = mail;
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassHash() {
		return passHash;
	}
	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
