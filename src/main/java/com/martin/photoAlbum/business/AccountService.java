package com.martin.photoAlbum.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.entities.Account;

public class AccountService extends Service {
	private static final String VALID_EMAIl_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	
	private Session session;
	private CategoryService categoryService;
	
	public AccountService() {
		this.categoryService = new CategoryService(session);
	}
	
	public RegisterResult register(String username, String password, String email, String name) {
		if (!validUsername(username)) {
			return new RegisterResult(RegisterStatus.INVALID_USERNAME);
		}
		
		if (!validPassword(password)) {
			return new RegisterResult(RegisterStatus.INVALID_PASSWORD);
		}
		
		if (!validEmail(email)) {
			return new RegisterResult(RegisterStatus.INVALID_EMAIL);
		}
		
		if (!validName(name)) {
			return new RegisterResult(RegisterStatus.INVALID_NAME);
		}
		
		Account acc = saveAccount(username, password, email, name);
		categoryService.add(name);
		
		return new RegisterResult(RegisterStatus.OK, acc);
	}
	
	private boolean validUsername(String username) {
		if (username == null || username.equals("")) {
			return false;
		}
		
		return username.matches("[a-zA-Z0-9]");
	}

	private boolean validPassword(String password) {
		if (password == null || password.length() < 6) {
			return false;
		}
		
		return true;
	}

	private boolean validEmail(String email) {
		if (email == null || email.equals("")) {
			return false;
		}
		
		return email.matches(VALID_EMAIl_PATTERN);
	}

	private boolean validName(String name) {
		if (name == null || name.equals("")) {
			return false;
		}
		
		return true;
	}

	public Account saveAccount(String username, String password, String email, String name) {
		Data data = Data.getInstance();
		
		String passHash = hash(password);
		
		Account acc = new Account(username, passHash, email, name);
		
		data.getEntityManager().persist(acc);
		data.getEntityManager().flush();
		
		return acc;
	}
	
	public Account login(String username, String password) {
		String passHash = hash(password);
		
		Account account = getAccount(username, passHash);
		
		return account;
	}
	
	public Account getAccount(String username, String passHash) {
		Query q = Data.getInstance().getEntityManager().createQuery(
				"SELECT a FROM Account a WHERE username = :username AND passHash = :passHash");
			q.setParameter("username", username);
			q.setParameter("passHash", passHash);
			try {
				return (Account) q.getSingleResult();
			} catch (NoResultException exc) {
				return null;
			}
	}
	
	public void logout() {
		
	}
	
	public EditProfileResult editProfile(String password, String email, String name) {
		if (!session.isLoggedIn()) {
			return EditProfileResult.NOT_LOGGED_IN;
		}
		
		Account account = session.getAccount();
		
		if (!validPassword(password)) {
			return EditProfileResult.INVALID_PASSWORD;
		}
		
		if (!validEmail(email)) {
			return EditProfileResult.INVALID_EMAIL;
		}
		
		if (!validName(name)) {
			return EditProfileResult.INVALID_NAME;
		}
		
		categoryService.renameParentCategory(account.getName(), name);
		
		String passHash = hash(password);
		
		account.setMail(email);
		account.setName(name);
		account.setPassHash(passHash);
		
		Data.getInstance().getEntityManager().persist(account);
		
		return EditProfileResult.OK;
	}
	
	public String hash(String str) {
        // Create MessageDigest instance for MD5
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return str;
		}
        //Add password bytes to digest
        md.update(str.getBytes());
        //Get the hash's bytes 
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        return sb.toString();
	}
	
	public Account getById(int id) {
		return Data.getInstance().getEntityManager().find(Account.class, id);
	}
	
	public static class RegisterResult {
		private RegisterStatus status;
		private Account account;
		
		public RegisterResult(RegisterStatus status) {
			this.status = status;
		}
		
		public RegisterResult(RegisterStatus status, Account account) {
			this(status);
			this.account = account;
		}

		public RegisterStatus getStatus() {
			return status;
		}

		public Account getAccount() {
			return account;
		}
	}
	
	public static enum RegisterStatus {
		INVALID_USERNAME, INVALID_PASSWORD, INVALID_EMAIL, INVALID_NAME, OK
	}
	
	public static enum EditProfileResult {
		NOT_LOGGED_IN, INVALID_PASSWORD, INVALID_EMAIL, INVALID_NAME, OK
		
	}
}
