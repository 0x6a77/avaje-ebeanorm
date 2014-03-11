package com.avaje.ebeaninternal.server.cache;

import java.util.List;

public class CachedManyIds {
	
    // JBW/GW - 12MAR13: Add a cache of the many's member object class types in case the many's target is abstract
	// (and therefore cannot be directly instantiated when we rehydrate members).

	private final List<AssocBeanCacheCombo> idList;
	
	public CachedManyIds(List<AssocBeanCacheCombo> idList) {
		this.idList = idList;
	}

	public List<AssocBeanCacheCombo> getIdList() {
    	return idList;
    }

}
