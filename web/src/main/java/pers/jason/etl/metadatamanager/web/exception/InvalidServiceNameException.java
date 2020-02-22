package pers.jason.etl.metadatamanager.web.exception;

/**
 * @author Jason
 * @date 2020/2/18 1:19
 * @description
 */
public class InvalidServiceNameException extends RuntimeException {

  private String serviceName;

  public InvalidServiceNameException(String serviceName) {
    super("无效的服务名：" + serviceName);
    this.serviceName = serviceName;
  }

  public String getInvalidServiceName() {
    return serviceName;
  }
}
