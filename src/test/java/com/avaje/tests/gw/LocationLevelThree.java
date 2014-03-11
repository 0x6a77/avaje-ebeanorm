package com.avaje.tests.gw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.avaje.ebean.annotation.CacheStrategy;

@Entity
@CacheStrategy(useBeanCache = true)
@DiscriminatorValue("THREE")
public class LocationLevelThree extends LocationABC {

	private static final long serialVersionUID = 1L;

	public LocationLevelThree() {
		
	}
	
	public LocationLevelThree(final String locationId) {
		super(locationId);
	}
	
	public void levelOneLogic() {
		System.out.println("Invoke level three logic");
	}

	public String toString() {
		return "Level3: " + id + " " + locationId;
	}

}
