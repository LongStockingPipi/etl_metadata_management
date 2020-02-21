package pers.jason.etl.rest.pojo.po;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.rest.pojo.MetadataType;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:08
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Metadata implements Serializable, Comparable {

  private Long id;

  private String name;

  private Date createdTime = new Date();

  private Long creator;

  private Date updatedTime = new Date();

  private Long updatedBy;

  private String comments;

  private String fullName;

  private Boolean status = true;

  public abstract MetadataType returnMetadataType();

  public abstract <T extends Metadata> Set<T> getChild();

  public abstract void setParentId(Long id);

  @Override
  public int compareTo(Object o) {
    if(!(o instanceof Metadata)){
      throw new RuntimeException("传入的数据类型不一致");
    }
    Metadata metadata =(Metadata)o;
    return this.fullName.compareTo(metadata.fullName);
  }

  public static void main(String[] args) {
    Metadata metadata1 = new ExternalPlatform();
    Metadata metadata2 = new ExternalSchema();
    Metadata metadata3 = new ExternalSchema();
    Metadata metadata4 = new ExternalSchema();
    Metadata metadata5 = new ExternalTable();

    metadata1.setFullName("/p_1");
    metadata2.setFullName("/p_1/s_test_010");
    metadata3.setFullName("/p_1/s_2");
    metadata4.setFullName("/p_1/s_test_99");
    metadata5.setFullName("/p_1/t_2/t1");

    List<Metadata> list = Lists.newArrayList(metadata1, metadata2, metadata3, metadata4, metadata5);
    Collections.sort(list);
    list.forEach(metadata -> {
        System.out.println(metadata.getFullName());
    });
  }

}
