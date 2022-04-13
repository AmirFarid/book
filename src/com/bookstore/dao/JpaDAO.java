package com.bookstore.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JpaDAO<Entity> {

	protected EntityManager entityManager;

	public JpaDAO(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public Entity create(Entity entity) {

		entityManager.getTransaction().begin();

		entityManager.persist(entity);
		entityManager.flush();
		entityManager.refresh(entity);

		entityManager.getTransaction().commit();

		return entity;
	}

	public Entity update(Entity entity) {
		System.out.println("2");
		entityManager.getTransaction().begin();
		System.out.println("3");
		entity = entityManager.merge(entity);
		System.out.println("4");
		entityManager.getTransaction().commit();
		System.out.println("5");
		return entity;
	}

	public Entity find(Class<Entity> type, Object id) {
		Entity entity = entityManager.find(type, id);
		if (entity != null)
			entityManager.refresh(entity);
		return entity;
	}
	
	public void delete(Class<Entity> type , Object id) {
		entityManager.getTransaction().begin();
		Object refrence =  entityManager.getReference(type, id);
		entityManager.remove(refrence);
		entityManager.getTransaction().commit();
		
	}
	
	public List<Entity> findWithNamedQuery(String queryName){
		Query query = entityManager.createNamedQuery(queryName);
		return query.getResultList();
	}
	
	public List<Entity> findWithNamedQuery(String queryName , String paramName , Object paramValue ){
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter(paramName, paramValue);
		return query.getResultList();
	}
	
	public List<Entity> findWithNamedQuery(String queryName , Map<String, Object> parameters){
		Query query = entityManager.createNamedQuery(queryName);
		
		Set<Entry<String,Object>> setParameters = parameters.entrySet(); 
		
		for (Entry<String,Object> entry : setParameters) {
		
		query.setParameter(entry.getKey(), entry.getValue());
		
		}
		return query.getResultList();
	}
	
	public long countWithNamedQuery(String queryName) {
		Query query = entityManager.createNamedQuery(queryName);
		return (long) query.getSingleResult();
	}

}
