package com.avaje.tests.gw;

import junit.framework.TestCase;

import com.avaje.ebean.Ebean;

public class RecursiveHierarchyWithAbstractClassTest extends TestCase {

	public void test() {

		LocationLevelOne node_1 = createLevelOne("1");

		LocationLevelTwo node_1_1 = createLevelTwo(node_1, "1.1");
		LocationLevelThree node_1_1_1 = createLevelThree(node_1_1, "1.1.1");
		LocationLevelThree node_1_1_2 = createLevelThree(node_1_1, "1.1.2");

		LocationLevelTwo node_1_2 = createLevelTwo(node_1, "1.2");
		LocationLevelThree node_1_2_1 = createLevelThree(node_1_1, "1.2.1");
		LocationLevelThree node_1_2_2 = createLevelThree(node_1_1, "1.2.2");

		for (LocationABC location : node_1.getChildren()) {
			System.out.println("Location: " + location.getLocationId());
			System.out.println("Parent location: " + location.getParent().getLocationId());
		}

		for (LocationABC location : node_1.getChildren()) {
			System.out.println("Location: " + location.getLocationId());
			System.out.println("Parent location: " + location.getParent().getLocationId());
		}

		LocationLevelOne foundNode_1 = (LocationLevelOne) node_1_1.getParent();
		for (LocationABC location : foundNode_1.getChildren()) {
			System.out.println("Location: " + location.getLocationId());
			System.out.println("Parent location: " + location.getParent().getLocationId());
		}
	}

	LocationLevelOne createLevelOne(String locationId) {
		LocationLevelOne result = null;
		result = new LocationLevelOne(locationId);
		Ebean.save(result);
		result = Ebean.getReference(LocationLevelOne.class, result.getId());
		return result;
	}

	LocationLevelTwo createLevelTwo(final LocationLevelOne levelOne, String locationId) {
		LocationLevelTwo result = null;
		result = new LocationLevelTwo(locationId);
		result.setParent(levelOne);
		Ebean.save(result);
		result = Ebean.getReference(LocationLevelTwo.class, result.getId());
		levelOne.addChildLocation(result);
		return result;
	}

	LocationLevelThree createLevelThree(final LocationLevelTwo levelTwo, String locationId) {
		LocationLevelThree result = null;
		result = new LocationLevelThree(locationId);
		result.setParent(levelTwo);
		Ebean.save(result);
		result = Ebean.getReference(LocationLevelThree.class, result.getId());
		levelTwo.addChildLocation(result);
		return result;
	}

}
