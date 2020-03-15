package pers.jason.etl.metadatamanager.core.support.util;

import pers.jason.etl.metadatamanager.core.support.exception.InvalidTableTypeException;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType;

import static pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType.BASE_TABLE;
import static pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType.SYSTEM_VIEW;
import static pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType.TABLE;
import static pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType.VIEW;

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
      if(TABLE.name.equals(typeName)) {
        return TABLE;
      }
      return ExternalTableType.valueOf(upperName);
    } catch (Exception e) {
      throw new InvalidTableTypeException(typeName);
    }
  }
}
