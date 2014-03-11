package com.avaje.ebeaninternal.server.query;

import java.sql.SQLException;
import java.util.List;

import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.ManyType;

public final class SqlTreeNodeManyRoot extends SqlTreeNodeBean {

  public SqlTreeNodeManyRoot(String prefix, BeanPropertyAssocMany<?> prop, SqlTreeProperties props, List<SqlTreeNode> myList) {
    super(prefix, prop, prop.getTargetDescriptor(), props, myList, true, null);
  }

  @Override
  protected void postLoad(DbReadContext cquery, Object loadedBean, Object id, Object lazyLoadParentId) {

    // put the localBean into the manyValue so that it
    // is added to the collection/map
    cquery.setLoadedManyBean(loadedBean);
  }

  @Override
	public void load(DbReadContext cquery, Object parentBean, boolean hasBeanMapKey) throws SQLException {
    // pass in null for parentBean because the localBean
    // that is built is added to a collection rather than
    // being set to the parentBean directly

	// JBW/GW - 13APR13: For beanmap turn off lazy load. It may find our bean in CTX with mapkey unloaded and unloadable.
	if (nodeBeanProp instanceof BeanPropertyAssocMany) {
	  BeanPropertyAssocMany many = (BeanPropertyAssocMany) nodeBeanProp;
      super.load(cquery, null, many.getManyType().equals(ManyType.JAVA_MAP));
	} else {
    	  super.load(cquery, null, false);
	}
  }

  /**
   * Force outer join for everything after the many property.
   */
  @Override
  public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin) {
    super.appendFrom(ctx, true);
  }

}
