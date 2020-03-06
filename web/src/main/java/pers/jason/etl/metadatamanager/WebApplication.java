package pers.jason.etl.metadatamanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pers.jason.etl.metadatamanager.core.support.props.CoreProperties;
import pers.jason.etl.metadatamanager.web.props.SimpleProperties;

@EnableConfigurationProperties(SimpleProperties.class)
@SpringBootApplication
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
