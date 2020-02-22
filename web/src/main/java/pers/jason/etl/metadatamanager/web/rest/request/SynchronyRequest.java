package pers.jason.etl.metadatamanager.web.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jason
 * @date 2020/2/18 0:46
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SynchronyRequest implements Serializable {

  private Integer syncType;

  private Integer platformType;

  private Long platformId;

  private Long schemaId;

  private Long tableId;

  private Long requestTime;
}
