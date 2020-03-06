package pers.jason.etl.metadatamanager.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pers.jason.etl.metadatamanager.core.support.props.CoreProperties;

/**
 * @author Jason
 * @date 2020/3/7 1:03
 * @description
 */
@Configuration
@EnableConfigurationProperties(CoreProperties.class)
public class CoreConfig {
}
