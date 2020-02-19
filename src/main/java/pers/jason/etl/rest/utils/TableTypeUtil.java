package pers.jason.etl.rest.utils;

import pers.jason.etl.rest.exception.InvalidTableTypeException;
import pers.jason.etl.rest.pojo.ExternalTableType;

import static pers.jason.etl.rest.pojo.ExternalTableType.BASE_TABLE;
import static pers.jason.etl.rest.pojo.ExternalTableType.SYSTEM_VIEW;
import static pers.jason.etl.rest.pojo.ExternalTableType.VIEW;

/**
 * @author Jason
 * @date 2020/2/18 0:38
 * @description
 */
public class TableTypeUtil {

  public static ExternalTableType getTableType(String typeName) {
    typeName = typeName.trim();
    String upperName = typeName.toUpperCase();
    try {
      if(BASE_TABLE.name.equals(typeName)) {
        return BASE_TABLE;
      }
      if(VIEW.name.equals(typeName)) {
        return VIEW;
      }
      if(SYSTEM_VIEW.name.equals(typeName)) {
        return SYSTEM_VIEW;
      }
      return ExternalTableType.valueOf(upperName);
    } catch (Exception e) {
      throw new InvalidTableTypeException(typeName);
    }
  }
}
