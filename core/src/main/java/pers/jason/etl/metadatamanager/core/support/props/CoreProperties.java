package pers.jason.etl.metadatamanager.core.support.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jason
 * @date 2020/3/7 0:51
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "etl.metadata")
public class CoreProperties {

  @Autowired(required = false)
  private Properties simple;

  private String lockType = "lock"; //加锁策略 lock(for single node) redis(for distributed)

  private Integer syncLockTimeOut = 15; //竞争同步锁等待超时时间（秒）

  private Boolean useCache = false; //同步过程是否使用缓存

  private Boolean checkPermission = false; //同步过程是否需要权限校验

}
