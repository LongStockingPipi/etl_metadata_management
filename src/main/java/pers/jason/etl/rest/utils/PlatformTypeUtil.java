package pers.jason.etl.rest.utils;

import pers.jason.etl.rest.exception.InvalidPlatformTypeException;
import pers.jason.etl.rest.pojo.PlatformType;

/**
 * @author Jason
 * @date 2020/2/18 1:11
 * @description
 */
public class PlatformTypeUtil {

  public static PlatformType getPlatformTypeByCode(Integer code) {
    if(PlatformType.MYSQL.code.equals(code)) {
      return PlatformType.MYSQL;
    } else if(PlatformType.ORACLE.code.equals(code)) {
      return PlatformType.ORACLE;
    } else {
      throw new InvalidPlatformTypeException(code);
    }
  }
}
