package com.martin.photoAlbum.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.martin.photoAlbum.Data;
import com.martin.photoAlbum.Session;
import com.martin.photoAlbum.entities.Account;

import dto.people.PeopleResult;
import dto.people.PersonResult;

public class AccountService extends Service {
	private static final String VALID_EMAIl_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

	private CategoryService categoryService;
	
	public AccountService() {
		this.categoryService = new CategoryService(session);
	}
	
	public RegisterResult register(String username, String password, String email, String name) {
		Account existingAccount = getAccount(username);
		
		if (existingAccount != null) {
			return new RegisterResult(RegisterStatus.USERNAME_ALREADY_EXISTS);
		}
		
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
		login(username, password);

		session.setAccount(acc);
		categoryService.add(name);
		
		return new RegisterResult(RegisterStatus.OK, acc);
	}
	
	private boolean validUsername(String username) {
		if (username == null || username.equals("")) {
			return false;
		}
		
		return username.matches("[a-zA-Z0-9]+");
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
	
	private Account getAccount(String username) {
		Query q = Data.getInstance().getEntityManager().createQuery(
				"SELECT a FROM Account a WHERE username = :username");
			q.setParameter("username", username);
			try {
				return (Account) q.getSingleResult();
			} catch (NoResultException exc) {
				return null;
			}
	}
	
	public void logout() {
		
	}

	public static enum ChangePasswordResult {
		NOT_LOGGED_IN, INVALID_PASSWORD, OK
	}

	public ChangePasswordResult changePassword(String password) {
		if (!session.isLoggedIn()) {
			return ChangePasswordResult.NOT_LOGGED_IN;
		}
		
		if (!validPassword(password)) {
			return ChangePasswordResult.INVALID_PASSWORD;
		}
		
		Account account = session.getAccount();

		String passHash = hash(password);
		account.setPassHash(passHash);
		
		return ChangePasswordResult.OK;
	}

	public EditProfileResult editProfile(String email, String name) {
		if (!session.isLoggedIn()) {
			return EditProfileResult.NOT_LOGGED_IN;
		}
		
		Account account = session.getAccount();
		
		if (!validEmail(email)) {
			return EditProfileResult.INVALID_EMAIL;
		}
		
		if (!validName(name)) {
			return EditProfileResult.INVALID_NAME;
		}
		
		categoryService.renameParentCategory(account.getName(), name);
		
		account.setMail(email);
		account.setName(name);
		
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
	
	public void setSession(Session session) {
		super.setSession(session);
		
		categoryService.session = session;
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
		INVALID_USERNAME, INVALID_PASSWORD, INVALID_EMAIL, INVALID_NAME, OK, USERNAME_ALREADY_EXISTS
	}
	
	public static enum EditProfileResult {
		NOT_LOGGED_IN, INVALID_PASSWORD, INVALID_EMAIL, INVALID_NAME, OK
		
	}

	public PeopleResult findPople(String term, int start, int limit) {
		PeopleResult peopleResult = new PeopleResult();
		Query q = Data.getInstance().getEntityManager().createQuery(
				"SELECT a FROM Account a WHERE username like :term OR name like :term LIMIT :start, :limit");
		q.setParameter("term", "%"+term+"%");
		q.setParameter("start", start);
		q.setParameter("limit", limit);

		int count = getResultCountFor(term);

		List<Account> accounts = q.getResultList();

		List<PersonResult> items = new ArrayList<>(limit);

		for (Account account : accounts) {
			PersonResult personResult = new PersonResult();

			personResult.setId(account.getId());
			personResult.setName(account.getName());
			personResult.setUsername(account.getUsername());

			items.add(personResult);
		}

		peopleResult.setItems(items);
		peopleResult.setIncompleteResults(false);
		peopleResult.setTotalCount(count);
		return peopleResult;
	}

	private int getResultCountFor(String term) {
		Query q = Data.getInstance().getEntityManager().createQuery(
				"SELECT count(a.id) FROM Account a WHERE username like :term OR name like :term");
		q.setParameter("term", "%"+term+"%");

		return (int)q.getSingleResult();
	}
}