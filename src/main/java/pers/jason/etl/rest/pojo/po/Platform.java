package pers.jason.etl.rest.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jason
 * @date 2020/2/18 0:56
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Platform extends Metadata {

  private String url;

  private String username;

  private String passwd;
}
