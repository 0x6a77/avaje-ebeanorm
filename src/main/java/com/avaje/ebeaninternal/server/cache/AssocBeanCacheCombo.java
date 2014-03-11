package com.avaje.ebeaninternal.server.cache;

/**
 * @author jeffw
 * 
 * When the association is abstract there is no way to know what kind of class to create when we get the item out of the cache.
 * This class maps that for us.
 * 
 * It might be easier if Ebean changed so that the beans themselves remembered what class they were before caching.
 *
 */
public class AssocBeanCacheCombo {

	private Object id;
	private Class clazz;

	public AssocBeanCacheCombo(Object id, Class clazz) {
		this.id = id;
		this.clazz = clazz;
	}
	
	public Object getId() {
		return id;
	}
	
	public Class getClazz() {
		return clazz;
	}

}