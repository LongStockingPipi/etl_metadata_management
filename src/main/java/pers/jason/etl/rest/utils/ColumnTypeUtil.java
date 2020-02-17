package pers.jason.etl.rest.utils;

import pers.jason.etl.rest.exception.InvalidFieldTypeException;
import pers.jason.etl.rest.pojo.MySqlColumnType;

/**
 * @author Jason
 * @date 2020/2/18 0:23
 * @description
 */
public class ColumnTypeUtil {

  public static MySqlColumnType getTypeByName(String name) {
    name = name.trim();
    String upperName = name.toUpperCase();
    MySqlColumnType type;
    try {
      type = MySqlColumnType.valueOf(upperName);
    } catch (Exception e) {
      throw new InvalidFieldTypeException(name);
    }
    return type;
  }

  public static void main(String[] args) {
    String typeName = "Int  ";
    System.out.println(ColumnTypeUtil.getTypeByName(typeName).code);
  }
}
