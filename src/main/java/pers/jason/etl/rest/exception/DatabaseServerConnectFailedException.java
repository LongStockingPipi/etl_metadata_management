package pers.jason.etl.rest.exception;

/**
 * @author Jason
 * @date 2020/2/18 21:47
 * @description
 */
public class DatabaseServerConnectFailedException extends RuntimeException {

  public DatabaseServerConnectFailedException() {
  }

  public DatabaseServerConnectFailedException(String message) {
    super(message);
  }
}
