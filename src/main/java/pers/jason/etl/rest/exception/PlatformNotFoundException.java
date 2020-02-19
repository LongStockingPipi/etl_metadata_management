package pers.jason.etl.rest.exception;

/**
 * @author Jason
 * @date 2020/2/20 1:01
 * @description
 */
public class PlatformNotFoundException extends RuntimeException {

  public PlatformNotFoundException() {
  }

  public PlatformNotFoundException(String message) {
    super(message);
  }
}
