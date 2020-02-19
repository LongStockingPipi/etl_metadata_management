package pers.jason.etl.rest.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jason
 * @date 2020/2/17 23:08
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata implements Serializable {

  private Long id;

  private String name;

  private Date createdTime = new Date();

  private Long creator;

  private Date updatedTime = new Date();

  private Long updatedBy;

  private String comments;

  private String fullName;

  private Boolean status = true;
}
