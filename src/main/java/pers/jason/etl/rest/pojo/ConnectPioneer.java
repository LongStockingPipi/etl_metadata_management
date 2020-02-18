package pers.jason.etl.rest.pojo;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Jason
 * @date 2020/2/18 1:26
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectPioneer {

  private String url;

  private String username;

  private String password;

  private String schemaName;

  private String tableName;

  private String driverName;

  private Map<String, String> props = Maps.newHashMap();

  public void addProperty(final String key, final String value) {
    if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
      if(null == props) {
        props = Maps.newHashMap();
      }
      props.put(key, value);
    }
  }

}
