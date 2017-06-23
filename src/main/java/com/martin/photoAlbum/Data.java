package com.martin.photoAlbum;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Data {
	private static final String PERSISTENCE_UNIT_NAME = "photoalbum";
	private EntityManager em;
	private static volatile Data instance;

	private Data() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
	}
	
	public EntityManager getEntityManager() {
		return em;
	}
	
	public static Data getInstance() {
		if (instance == null) {
			synchronized(Data.instance) {
				if (instance == null) {
					instance = new Data();
				}
			}
		}
		
		return instance;
	}
}
