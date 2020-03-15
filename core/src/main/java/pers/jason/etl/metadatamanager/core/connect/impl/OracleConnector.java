package pers.jason.etl.metadatamanager.core.connect.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pers.jason.etl.metadatamanager.core.connect.ConnectPioneer;
import pers.jason.etl.metadatamanager.core.connect.DatabaseConnector;
import pers.jason.etl.metadatamanager.core.support.Symbol;
import pers.jason.etl.metadatamanager.core.support.exception.DatabaseServerConnectFailedException;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jason
 * @date 2020/3/15 21:52
 * @description
 */
@Slf4j
@Component
public class OracleConnector implements DatabaseConnector {

  private static final String USER_EXIST = "select count(*) as count from DBA_USERS where USERNAME = '%s'";

  private static final String USER_AND_TABLE_EXIST
      = "select count(*) as count from ALL_TABLES where OWNER = '%s' and TABLE_NAME = '%s'";

  private static final String SQL_RESULT_COLUMN_LABEL_IS_EXIST = "count";

  private static final Integer SINGLE_RESULT = 1;

  @Override
  public void tryConnect(ConnectPioneer pioneer) {
    //检测驱动
    try {
      Class.forName(pioneer.getDriverName());
      log.info("drive load successful:" + getConnectMessage(pioneer));
    } catch (ClassNotFoundException e) {
      log.error(e.getMessage(), e);
      String message = "driver load failure:" + getConnectMessage(pioneer);
      log.info(message);
      throw new DatabaseServerConnectFailedException(message);
    }

    String schemaName = pioneer.getSchemaName();
    if(StringUtils.isEmpty(schemaName)) {
      log.info("schema [" + schemaName + "] does not exist");
      throw new DatabaseServerConnectFailedException("schema [" + schemaName + "] does not exist");
    }

    String url = pioneer.getUrl();
    try (Connection connection = DriverManager.getConnection(url, pioneer.getUsername(), pioneer.getPassword())) {
      String tableName = pioneer.getTableName();
      String sql = StringUtils.isEmpty(tableName) ?
          String.format(USER_EXIST, schemaName) : String.format(USER_AND_TABLE_EXIST, schemaName, tableName);
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()) {
        int tableExist = resultSet.getInt(SQL_RESULT_COLUMN_LABEL_IS_EXIST);
        if(tableExist != SINGLE_RESULT) {
          log.info("database connection failed:schema(or table) [" + schemaName + "." + tableName + "] does not exist");
          throw new DatabaseServerConnectFailedException("schema(or table) [" + schemaName + "." + tableName + "] does not exist");
        }
      }
      log.info("database connection successful");
    } catch (SQLException e) {
      String message = "database connection failed: sqlstate[" + e.getSQLState() + "];message[" + e.getMessage() + "]";
      log.info(message);
      throw new DatabaseServerConnectFailedException(message);
    }
  }

  @Override
  public void tryConnect(ConnectPioneer pioneer, long timeout) {
    this.tryConnect(pioneer);
  }

  private String getConnectMessage(ConnectPioneer pioneer) {
    String url = pioneer.getUrl();
    String username = pioneer.getUsername();
    String password = pioneer.getPassword();
    return new StringBuilder(url).append(Symbol.SIGN_SEMICOLON).append(username)
        .append(Symbol.SIGN_SEMICOLON).append(password).toString();
  }
}
