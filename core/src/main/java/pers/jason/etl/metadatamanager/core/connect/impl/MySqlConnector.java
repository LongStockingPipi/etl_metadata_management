package pers.jason.etl.metadatamanager.core.connect.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date 2020/2/22 23:18
 * @description
 */
@Component
public class MySqlConnector implements DatabaseConnector {

  private static final Logger logger = LoggerFactory.getLogger(MySqlConnector.class);

  private static final String SQL_EXIST_TABLE = "select exists(select * from INFORMATION_SCHEMA.TABLES where table_schema = '%s' and table_name = '%s') as is_exist";

  private static final String SQL_RESULT_COLUMN_LABEL_IS_EXIST = "is_exist";

  @Override
  public void tryConnect(ConnectPioneer pioneer) {
    //检测驱动
    String driverName = pioneer.getDriverName();
    try {
      Class.forName(driverName);
      logger.info("drive load successful:" + getConnectMessage(pioneer));
    } catch (ClassNotFoundException e) {
      logger.error(e.getMessage(), e);
      String message = "driver load failure:" + getConnectMessage(pioneer);
      logger.info(message);
      throw new DatabaseServerConnectFailedException(message);
    }
    //连接server
    String url = MetadataUtil.getUrlByPioneer(pioneer);
    try (Connection connection = DriverManager.getConnection(url, pioneer.getUsername(), pioneer.getPassword())) {
      String tableName = pioneer.getTableName();
      if(StringUtils.isNotEmpty(tableName)) {
        String schemaName = pioneer.getSchemaName();
        String sql = String.format(SQL_EXIST_TABLE, schemaName, tableName);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
          boolean tableExist = resultSet.getBoolean(SQL_RESULT_COLUMN_LABEL_IS_EXIST);
          if(!tableExist) {
            logger.info("database connection failed:table [" + schemaName + "." + tableName + "] does not exist");
            throw new DatabaseServerConnectFailedException("table [" + schemaName + "." + tableName + "] does not exist");
          }
        }
      }
      logger.info("database connection successful");
    } catch (SQLException e) {
      String message = "database connection failed: sqlstate[" + e.getSQLState() + "];message[" + e.getMessage() + "]";
      logger.info(message);
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
    return new StringBuilder(url).append(Symbol.SIGN_SEMICOLON).append(username).append(Symbol.SIGN_SEMICOLON).append(password).toString();
  }
}
