package com.avaje.tests.embedded;

import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.tests.model.basic.ResetBasicData;
import com.avaje.tests.model.embedded.EMain;
import com.avaje.tests.model.embedded.Eembeddable;

public class TestEmbedded {

	@Test
	public void test() {
		ResetBasicData.reset();
		ServerCacheManager serverCacheManager = Ebean.getServerCacheManager();
		serverCacheManager.clearAll();

		EMain main = new EMain();
		main.setName("name");
		
		Eembeddable embeddable = new Eembeddable();
		embeddable.setDescription("desc");
		
		main.setEmbeddable(embeddable);
		Ebean.save(main);
		
		Eembeddable embeddable2 = new Eembeddable();
		embeddable2.setDescription("desc2");
		Ebean.save(main);
	}

}
