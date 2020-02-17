package pers.jason.etl.rest.utils;

import pers.jason.etl.rest.exception.InvalidTableTypeException;
import pers.jason.etl.rest.pojo.ExternalTableType;

/**
 * @author Jason
 * @date 2020/2/18 0:38
 * @description
 */
public class TableTypeUtil {

  public static ExternalTableType getTableType(String typeName) {
    typeName = typeName.trim();
    String upperName = typeName.toUpperCase();
    ExternalTableType type;
    try {
      type = ExternalTableType.valueOf(upperName);
    } catch (Exception e) {
      throw new InvalidTableTypeException(typeName);
    }
    return type;
  }
}
