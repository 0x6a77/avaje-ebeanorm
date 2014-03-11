package com.avaje.tests.gw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.avaje.ebean.annotation.CacheStrategy;

@Entity
@CacheStrategy(useBeanCache = true)
@DiscriminatorValue("TWO")
public class LocationLevelTwo extends LocationABC {

	private static final long serialVersionUID = 1L;

	public LocationLevelTwo() {
		
	}
	
	public LocationLevelTwo(final String locationId) {
		super(locationId);
	}
	
	public void levelOneLogic() {
		System.out.println("Invoke level two logic");
	}

	public String toString() {
		return "Level2: " + id + " " + locationId;
	}

}
