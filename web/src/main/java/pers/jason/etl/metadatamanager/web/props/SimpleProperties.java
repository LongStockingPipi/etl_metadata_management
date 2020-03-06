package pers.jason.etl.metadatamanager.web.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pers.jason.etl.metadatamanager.core.support.props.Properties;

/**
 * @author Jason
 * @date 2020/3/7 1:32
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "etl.metadata.distributed")
public class SimpleProperties implements Properties {

  private Long distributedLockTimeout = 15000L; //依赖锁竞争强度

  private Long lockExpireTime = 10L; //依赖平均执行时间

}
