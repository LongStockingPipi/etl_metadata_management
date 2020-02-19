package pers.jason.etl.rest.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jason
 * @date 2020/2/20 0:51
 * @description
 */
public class JdbcUtil {

  public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
    if(null != resultSet) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if(null != preparedStatement) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if(null != connection) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
