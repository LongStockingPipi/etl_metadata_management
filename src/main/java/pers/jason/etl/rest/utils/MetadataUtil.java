package pers.jason.etl.rest.utils;

import pers.jason.etl.commons.Symbol;
import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.pojo.VirtualPlatform;

import java.util.Map;

/**
 * @author Jason
 * @date 2020/2/18 1:22
 * @description
 */
public class MetadataUtil {

  public static final String DRIVER_NAME_MYSQL = "com.mysql.jdbc.Driver";

  public static final String DRIVER_NAME_ORACLE = "com.mysql.jdbc.Driver";

  public static final String URL_PREFIX_MYSQL = "jdbc:mysql://";

  public static VirtualPlatform getVirtualPlatform() {
    return new VirtualPlatform();
  }

  public static String getDriverNameByType(PlatformType type) {
    if(PlatformType.MYSQL.equals(type)) {
      return DRIVER_NAME_MYSQL;
    } else if(PlatformType.ORACLE.equals(type)) {
      return DRIVER_NAME_ORACLE;
    } else {
      return null;
    }
  }

  public static String joinProps(final Map<String, String> props) {
    StringBuilder sb = new StringBuilder();
    if(null != props && props.size() > 0) {
      for(Map.Entry entry : props.entrySet()) {
        sb.append(entry.getKey()).append(Symbol.SIGN_EQUALS).append(entry.getValue()).append(Symbol.SIGN_AND);
      }
    }
    return sb.toString();
  }
}
