package com.martin.photoAlbum.requesthandlers;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

import com.martin.photoAlbum.business.AccountService;
import com.martin.photoAlbum.business.facerecognition.FaceService;
import com.martin.photoAlbum.entities.Account;
import com.martin.photoAlbum.entities.Face;

import dto.people.PeopleResult;

public class FaceApiService extends ApiService<AccountService> {
	private final static int RESULTS_PER_PAGE = 15;
	private AccountService accService = new AccountService();
	public FaceApiService() {
		super(new AccountService());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FaceService faceService;

	@PUT
	public void assign(int faceID, int accountID) {
		Face face = faceService.getByID(faceID);

		Account account = service.getById(accountID);

		face.setPerson(account);
	}

	@GET
	public Response findPeople(String q, Integer pageObj) {
		int page = pageObj == null ? 1 : pageObj;

		PeopleResult result = accService.findPople(q, (page-1)*RESULTS_PER_PAGE, RESULTS_PER_PAGE);

		return Response.status(200).entity(result).build();

	}
}