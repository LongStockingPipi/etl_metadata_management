package pers.jason.etl.metadatamanager.core.synchronize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.metadatamanager.core.support.MetadataType;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Metadata metadata = (Metadata) o;
    return Objects.equals(fullName, metadata.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName);
  }
}
