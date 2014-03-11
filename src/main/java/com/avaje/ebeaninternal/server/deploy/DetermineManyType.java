package com.avaje.ebeaninternal.server.deploy;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Determine the Many Type for a property.
 */
public class DetermineManyType {

  public DetermineManyType() {
  }

  public ManyType getManyType(Class<?> type) {
    // JBW/GW - 23OCT12: allow sub classes of List
    if (List.class.isAssignableFrom(type)){
      return ManyType.JAVA_LIST;
    }
    // JBW/GW - 23OCT12: allow sub classes of Set
    if (Set.class.isAssignableFrom(type)){
      return ManyType.JAVA_SET;
    }
    // JBW/GW - 23OCT12: allow sub classes of Map
    if (Map.class.isAssignableFrom(type)){
      return ManyType.JAVA_MAP;
    }
    return null;
  }
}
