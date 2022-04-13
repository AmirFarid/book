package com.bookstore.dao;

import java.util.List;

public interface GenericDAO<Entity> {
	
	public Entity create(Entity entity);
	
	public Entity update(Entity entity);
	
	public Entity get(Object id);
	
	public void delete(Object id);
	
	public List<Entity> listAll();
	
	public long count();

}
