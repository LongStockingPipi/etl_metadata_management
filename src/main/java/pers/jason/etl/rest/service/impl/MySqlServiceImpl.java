package pers.jason.etl.rest.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pers.jason.etl.commons.Symbol;
import pers.jason.etl.rest.exception.DatabaseServerConnectFailedException;
import pers.jason.etl.rest.pojo.ConnectPioneer;
import pers.jason.etl.rest.service.DatabaseService;
import pers.jason.etl.rest.utils.MetadataUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Jason
 * @date 2020/2/18 0:50
 * @description
 */
@Service
public class MySqlServiceImpl implements DatabaseService {

  private static final Logger logger = LoggerFactory.getLogger(MySqlServiceImpl.class);

  @Override
  public boolean connect(ConnectPioneer pioneer) {
    String url = MetadataUtil.URL_PREFIX_MYSQL + pioneer.getUrl();
    String username = pioneer.getUsername();
    String password = pioneer.getPassword();
    String schemaName = pioneer.getSchemaName();
    if(StringUtils.isNoneEmpty(schemaName)) {
      url = url + "/" + schemaName;
    }
    Map<String, String> props = pioneer.getProps();
    if(null != props && props.size() > 0) {
      url = url + "?" + MetadataUtil.joinProps(props);
    }
    String driverName = pioneer.getDriverName();
    try {
      Class.forName(driverName);
      logger.info("drive load successful:" + getConnectMessage(url, username, password));
    } catch (ClassNotFoundException e) {
      logger.error(e.getMessage(), e);
      logger.info("driver load failure:" + getConnectMessage(url, username, password));
      return false;
    }
    try (Connection connection = DriverManager.getConnection(url, username, password)) {
      String tableName = pioneer.getTableName();
      if(StringUtils.isNotEmpty(tableName)) {
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
      return true;
    } catch (SQLException e) {
      logger.info("database connection failed: sqlstate[" + e.getSQLState() + "];message[" + e.getMessage() + "]");
      return false;
    }
  }

  private String getConnectMessage(String url, String username, String password) {
    return new StringBuilder(url).append(Symbol.SIGN_SEMICOLON).append(username).append(Symbol.SIGN_SEMICOLON).append(password).toString();
  }
}
