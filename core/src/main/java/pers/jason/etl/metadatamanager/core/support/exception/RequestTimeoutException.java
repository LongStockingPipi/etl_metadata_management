package pers.jason.etl.metadatamanager.core.support.exception;

/**
 * @author Jason
 * @date 2020/2/19 19:40
 * @description
 */
public class RequestTimeoutException extends RuntimeException {

  public RequestTimeoutException() {
  }

  public RequestTimeoutException(String message) {
    super(message);
  }
}
