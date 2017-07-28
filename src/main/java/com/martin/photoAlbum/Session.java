package com.martin.photoAlbum;

import com.martin.photoAlbum.entities.Account;

public class Session {

	private Account loggedInAcc;
	
	public Account getAccount() {
		return loggedInAcc;
	}
	
	public boolean isLoggedIn() {
		return loggedInAcc != null;
	}
	
	public void setAccount(Account acc) {
		loggedInAcc = acc;
	}
}
