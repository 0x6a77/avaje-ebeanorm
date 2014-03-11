package com.avaje.tests.gw;

import java.sql.Timestamp;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.avaje.ebean.BeanState;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.tests.model.basic.OrderDetail;
import com.avaje.tests.model.basic.Order;
import com.avaje.tests.model.basic.ResetBasicData;

public class TestManyAfterCacheUpdate extends TestCase {

	public void test() {

		ResetBasicData.reset();
		ServerCacheManager serverCacheManager = Ebean.getServerCacheManager();
		serverCacheManager.clearAll();
		serverCacheManager.setCaching(Order.class, true);
		serverCacheManager.setCaching(OrderDetail.class, true);

		// Select two beans of the same order (both should reference the same cache item).

		List<Order> orders = Ebean.find(Order.class).findList();
		Integer orderId = orders.get(0).getId();

		Order order1 = Ebean.find(Order.class)
		        .setId(orderId)
		        .select("updtime")
		        .findUnique();

		Order order2 = Ebean.find(Order.class)
		        .setId(orderId)
		        .select("updtime")
		        .findUnique();

		// Verify that both beans are clean.
		BeanState beanState1 = Ebean.getBeanState(order1);
		Assert.assertFalse(beanState1.isNew());
		Assert.assertFalse(beanState1.isDirty());
		Assert.assertFalse(beanState1.isNewOrDirty());
		Assert.assertNotNull(beanState1.getLoadedProps());

		BeanState beanState2 = Ebean.getBeanState(order2);
		Assert.assertFalse(beanState2.isNew());
		Assert.assertFalse(beanState2.isDirty());
		Assert.assertFalse(beanState2.isNewOrDirty());
		Assert.assertNotNull(beanState2.getLoadedProps());

		// Verify the contacts count for both references to the bean.
		Assert.assertEquals(3, order1.getDetails().size());
		Assert.assertEquals(3, order2.getDetails().size());

		// Create a new contact and add it to the bean using one of the
		// references.
		OrderDetail newOrderDetail = new OrderDetail();
		newOrderDetail.setOrder(order1);
		newOrderDetail.setOrderQty(1);
		newOrderDetail.setShipQty(1);
		order1.addDetail(newOrderDetail);
		Ebean.save(newOrderDetail);

		// Now reselect the bean into the two references (both should refer
		// to the same cache item).
		order1 = Ebean.find(Order.class)
		        .setId(orderId)
		        .select("updtime")
		        .findUnique();

		order2 = Ebean.find(Order.class)
		        .setId(orderId)
		        .select("updtime")
		        .findUnique();

		// Verify that both beans are clean.
		beanState1 = Ebean.getBeanState(order1);
		Assert.assertFalse(beanState1.isNew());
		Assert.assertFalse(beanState1.isDirty());
		Assert.assertFalse(beanState1.isNewOrDirty());
		Assert.assertNotNull(beanState1.getLoadedProps());

		beanState2 = Ebean.getBeanState(order2);
		Assert.assertFalse(beanState2.isNew());
		Assert.assertFalse(beanState2.isDirty());
		Assert.assertFalse(beanState2.isNewOrDirty());
		Assert.assertNotNull(beanState2.getLoadedProps());

		// THIS IS THE FIRST PART THAT IS THE KEY TO WHAT I DON"T
		// UNDERSTAND.
		// AT THIS POINT BOTH BEANS SEE THAT THEY NEED TO LAZY LOAD THE
		// CONTACTS REFERENCE.
		// ESSENTIALLY IT SEES THE GRAPH IS NOT FRESH OR UP-TO-DATE.
		Assert.assertEquals(4, order1.getDetails().size());
		Assert.assertEquals(4, order2.getDetails().size());

		// Now create a new contact for the bean using one of the bean
		// references.
		OrderDetail newDetail2 = new OrderDetail();
		newDetail2.setOrder(order1);
		newDetail2.setOrderQty(2);
		newDetail2.setShipQty(2);
		order1.addDetail(newDetail2);
		Ebean.save(newDetail2);
		
		// A compromise on cache "staleness" is to have the app signal when it changed "many" properties by re-saving the @Version to signal cache staleness.
		order1.setUpdtime(new Timestamp(order1.getUpdtime().getTime()));
		Ebean.save(order1);

		// THIS IS THE OTHER PART THAT IS KEY TO WHAT I DON'T UNDERSTAND.
		// WHEN EBEAN CREATES THE GRAPH FROM LAZY LOAD, IT KNOWS THE
		// CONTACTS REFERENCE IS NOT FRESH.
		// BUT ON SECOND USAGE (WHEN THE BEAN IS DIRTY) IT NO LONGER HAS ANY
		// EFFECT.
		// YOU HAVE STALE DATA, BUT NO POSSIBLE WAY TO SEE THIS FROM THE
		// OBJECT GRAPH.
		Assert.assertEquals(5, order1.getDetails().size());
		Assert.assertEquals(5, order2.getDetails().size());

		// My assertion is the first time you access a property you get
		// something from the cache and you know it's fresh.
		// ANy subsequent time you access a property Ebean only checks the
		// "loaded" property. If it's dirty in the cache there is no way to
		// know.
		// In-memory bean references are essentially cutoff from the cache
		// once loaded.

		// This might be OK for POST/GET web-pages where every page-load
		// happens from bean IDs.
		// For SPA webapps and desktop apps that hold bean references
		// for a long time, there is no way to know if the bean
		// reference is stale or not. It seems you cannot use/trust the
		// object graph at all in these cases.
		
		// THIS UNIT TEST NOW PASSES - THE ABOVE PROBLEM WAS SOLVED.

	}
}
