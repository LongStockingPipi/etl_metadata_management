package pers.jason.etl.rest.utils;

import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.pojo.VirtualPlatform;

/**
 * @author Jason
 * @date 2020/2/18 1:22
 * @description
 */
public class MetadataUtil {

  private static final String DRIVER_NAME_MYSQL = "com.mysql.jdbc.Driver";

  private static final String DRIVER_NAME_ORACLE = "com.mysql.jdbc.Driver";

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
}
