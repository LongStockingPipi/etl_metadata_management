package pers.jason.etl.metadatamanager.core.connect;

/**
 * @author Jason
 * @date 2020/2/22 23:00
 * @description
 */
public interface DatabaseConnector {

  void tryConnect(ConnectPioneer pioneer);

  void tryConnect(ConnectPioneer pioneer, long timeout);


}
