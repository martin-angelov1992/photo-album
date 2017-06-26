package com.martin.photoAlbum;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Data {
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	private static final String PERSISTENCE_UNIT_NAME = "photoalbum";
	private ThreadLocal<EntityManager> emThreadLocal = new ThreadLocal<EntityManager>() {
	    @Override protected EntityManager initialValue() {
	        return emf.createEntityManager();
	    }
	};
	private static volatile Data instance;

	private Data() {}
	
	public EntityManager getEntityManager() {
		return emThreadLocal.get();
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
