package com.avaje.tests.gw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.avaje.ebean.annotation.CacheStrategy;

/**
 * Order entity bean.
 */
@Entity
@CacheStrategy(useBeanCache = true)
@DiscriminatorValue("ONE")
public class LocationLevelOne extends LocationABC {

	private static final long serialVersionUID = 1L;

	public LocationLevelOne() {
		
	}
	
	public LocationLevelOne(final String locationId) {
		super(locationId);
	}
	
	public void levelOneLogic() {
		System.out.println("Invoke level one logic");
	}

	public String toString() {
		return "Level1: " + id + " " + locationId;
	}

}
